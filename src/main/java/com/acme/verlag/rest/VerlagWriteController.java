/*
 * Copyright (c) 2023 - present Florian Sauer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.acme.verlag.rest;

import com.acme.verlag.service.ConstraintViolationsException;
import com.acme.verlag.service.VerlagWriteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.util.UUID;

import static com.acme.verlag.rest.VerlagGetController.*;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Controller
@RequiredArgsConstructor
@RequestMapping(REST_PATH)
@Slf4j
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075"})
public class VerlagWriteController {

    private static final String PROBLEM_PATH = "/problem/";
    private final VerlagMapper mapper;

    /**
     * Service für Verlage.
     */
    private final VerlagWriteService service;

    /**
     * Einen neuen Verlag-Datensatz anlegen.
     *
     * @param verlagDTO Das Verlagsobjekt aus dem eingegangenen Request-Body.
     * @param request   Das Request-Objekt, um Location im Response-Header zu erstellen.
     * @return Response mit Statuscode 201 einschließlich Location-Header oder Statuscode 422, falls mindestens ein
     * Constraint verletzt ist oder Statuscode 400, falls syntaktische Fehler im Request-Body vorliegen.
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> post(
        @RequestBody final VerlagDTO verlagDTO,
        final HttpServletRequest request
    ) {
        log.debug("post: {}", verlagDTO);
        final var verlagInput = mapper.toVerlag(verlagDTO);
        final var verlag = service.create(verlagInput);
        final var baseUri = request.getRequestURL();
        final var location = URI.create(STR."\{baseUri}/\{verlag.getId()}");
        return created(location).build();
    }

    /**
     * Einen vorhandenen Verlag-Datensatz überschreiben.
     *
     * @param id        Die ID des zu aktualisierenden Verlags.
     * @param verlagDTO Das Verlagsobjekt aus dem eingegangenen Request-Body.
     */
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    void put(@PathVariable final UUID id,
             @RequestBody final VerlagDTO verlagDTO
    ) {
        log.debug("put: id={}, {}", id, verlagDTO);
        final var verlagInput = mapper.toVerlag(verlagDTO);
        service.update(verlagInput, id);
    }

    @ExceptionHandler
    ProblemDetail onConstraintViolations(
        final ConstraintViolationsException ex,
        final HttpServletRequest request
    ) {
        log.debug("onConstraintViolations: {}", ex.getMessage());
        final var verlagViolations = ex.getViolations().stream()
            .map(violation -> STR."\{violation.getPropertyPath()}: " +
                STR."\{violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()} " +
                violation.getMessage())
            .toList();
        log.trace("onConstraintViolations: {}", verlagViolations);
        final String detail;
        if (verlagViolations.isEmpty()) {
            detail = "N/A";
        } else {
            // Entferne "[" und "]" aus dem String.
            final var violationsStr = verlagViolations.toString();
            detail = violationsStr.substring(1, violationsStr.length() - 2);
        }
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, detail);
        problemDetail.setType(URI.create(STR."\{PROBLEM_PATH}\{ProblemType.CONSTRAINTS.getValue()}"));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }
}
