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

package com.acme.verlag.graphql;

import com.acme.verlag.entity.Verlag;
import com.acme.verlag.service.ConstraintViolationsException;
import com.acme.verlag.service.VerlagWriteService;
import graphql.GraphQLError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.graphql.execution.ErrorType.BAD_REQUEST;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VerlagMutationController {

    private final VerlagWriteService service;
    private final VerlagInputMapper mapper;

    /**
     * Einen neuen Verlag anlegen.
     *
     * @param input Die Eingabedaten für einen neuen Verlag.
     * @return Die generierte ID für den neuen Verlag als Payload.
     */
    @MutationMapping
    CreatePayload create(@Argument final VerlagInput input) {
        log.debug("create: input={}", input);
        final var verlagNew = mapper.toVerlag(input);
        final var id = service.create(verlagNew).getId();
        log.debug("create: id={}", id);
        return new CreatePayload(id);
    }

    /*
        @GraphQlExceptionHandler
    @SuppressWarnings("unused")
    GraphQLError onDateTimeParseException(final DateTimeParseException ex) {
        final List<Object> path = List.of("input", "geburtsdatum");
        return GraphQLError.newError()
            .errorType(BAD_REQUEST)
            .message(STR."Das Datum \{ex.getParsedString()} ist nicht korrekt.")
            .path(path)
            .build();
    }
     */

    @GraphQlExceptionHandler
    @SuppressWarnings("unused")
    Collection<GraphQLError> onConstraintViolations(final ConstraintViolationsException ex) {
        return ex.getViolations()
            .stream()
            .map(this::violationToGraphQLError)
            .collect(Collectors.toList());
    }

    private GraphQLError violationToGraphQLError(final ConstraintViolation<Verlag> violation) {
        final List<Object> path = new ArrayList<>(5);
        path.add("input");
        for (final Path.Node node : violation.getPropertyPath()) {
            path.add(node.toString());
        }
        return GraphQLError.newError()
            .errorType(BAD_REQUEST)
            .message(violation.getMessage())
            .path(path)
            .build();
    }
}
