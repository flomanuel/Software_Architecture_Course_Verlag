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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static com.acme.verlag.rest.VerlagGetController.*;
import static org.springframework.http.HttpStatus.PRECONDITION_REQUIRED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.http.HttpStatus.NO_CONTENT;


@Controller
@RequiredArgsConstructor
@RequestMapping(REST_PATH)
@Slf4j
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075"})
public class VerlagWriteController {

    static final String PROBLEM_PATH = "/problem/";
    private static final String VERSIONSNUMMER_FEHLT = "Versionsnummer fehlt";
    private final VerlagMapper mapper;
    private final UriHelper uriHelper;

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
    @Operation(summary = "Einen neuen Verlag anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Verlag neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte")
    ResponseEntity<Void> post(
        @RequestBody final VerlagDTO verlagDTO,
        final HttpServletRequest request
    ) {
        log.debug("post: {}", verlagDTO);
        final var verlagInput = mapper.toVerlag(verlagDTO);
        final var verlag = service.create(verlagInput);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var location = URI.create(STR."\{baseUri}/\{verlag.getId()}");
        return created(location).build();
    }

    /**
     * Einen vorhandenen Verlag-Datensatz überschreiben.
     *
     * @param id        Die ID des zu aktualisierenden Verlags.
     * @param verlagUpdateDTO Das Verlagsobjekt aus dem eingegangenen Request-Body.
     */
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Einen Verlag mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Verlag nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte")
    ResponseEntity<Void> put(@PathVariable final UUID id,
                             @RequestBody final VerlagUpdateDTO verlagUpdateDTO,
                             @RequestHeader("If-Match") final Optional<String> version,
                             final HttpServletRequest request
    ) {
        log.debug("put: id={}, verlagUpdateDTO={}", id, verlagUpdateDTO);
        final int versionInt = getVersion(version, request);
        final var verlagInput = mapper.toVerlag(verlagUpdateDTO);
        final var verlag = service.update(verlagInput, id, versionInt);
        log.debug("put: {}", verlag);
        return noContent().eTag(STR."\"\{verlag.getVersion()}\"").build();
    }

    private int getVersion(final Optional<String> versionOpt, final HttpServletRequest request) {
        log.trace("getVersion: {}", versionOpt);
        if (versionOpt.isEmpty()) {
            throw new VersionInvalidException(
                PRECONDITION_REQUIRED,
                VERSIONSNUMMER_FEHLT,
                URI.create(request.getRequestURL().toString()));
        }
        final var versionStr = versionOpt.get();
        if (versionStr.length() < 3 ||
            versionStr.charAt(0) != '"' ||
            versionStr.charAt(versionStr.length() - 1) != '"') {
            throw new VersionInvalidException(
                PRECONDITION_FAILED,
                STR."Ungueltiges ETag \{versionStr}",
                URI.create(request.getRequestURL().toString())
            );
        }
        final int version;
        try {
            version = Integer.parseInt(versionStr.substring(1, versionStr.length() - 1));
        } catch (final NumberFormatException ex) {
            throw new VersionInvalidException(
                PRECONDITION_FAILED,
                STR."Ungueltiges ETag \{versionStr}",
                URI.create(request.getRequestURL().toString()),
                ex
            );
        }
        log.trace("getVersion: version={}", version);
        return version;
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
