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

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import static com.acme.verlag.rest.ProblemType.PRECONDITION;

import java.net.URI;

import static com.acme.verlag.rest.VerlagWriteController.PROBLEM_PATH;

/**
 * Exception, falls die Versionsnummer im ETag fehlt oder syntaktisch ungültig ist.
 */
class VersionInvalidException extends ErrorResponseException {

    VersionInvalidException(final HttpStatusCode status, final String message, final URI uri) {
        this(status, message, uri, null);
    }

    VersionInvalidException(
        final HttpStatusCode status,
        final String message,
        final URI uri,
        final Throwable cause
    ) {
        super(status, asProblemDetail(status, message, uri), cause);
    }

    private static ProblemDetail asProblemDetail(final HttpStatusCode status, final String detail, final URI uri) {
        final var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setType(URI.create(STR."\{PROBLEM_PATH}\{PRECONDITION.getValue()}"));
        problemDetail.setInstance(uri);
        return problemDetail;
    }
}