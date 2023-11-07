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

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.acme.verlag.rest.VerlagGetController.*;

/**
 * Hilfsklasse um URIs in ProblemDetail zu ermitteln, falls ein API-Gateway verwendet wird.
 */

@Component
@Slf4j
public class UriHelper {

    private static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
    private static final String X_FORWARDED_HOST = "x-forwarded-host";
    private static final String X_FORWARDED_PREFIX = "x-forwarded-prefix";
    private static final String VERLAGE_PREFIX = "/verlage";

    /**
     * Basis-URI ermitteln.
     *
     * @param request Servlet-Request
     * @return Die Basis-URI als String
     */
    URI getBaseUri(final HttpServletRequest request) {
        final var forwardedHost = request.getHeader(X_FORWARDED_HOST);
        if (forwardedHost != null) {
            // Falls durch Kubernetes Ingress Controller oder Spring Cloud Gateway der request weitergeleitet wurde.
            return getBaseUriForwarded(request, forwardedHost);
        }

        // KEIN Forwarding von einem API-Gateway. Der interne Hostname kann ein anderer sein als der externe Hostname.
        final var uriComponents = ServletUriComponentsBuilder.fromRequestUri(request).build();
        final var scheme = uriComponents.getScheme();
        final var host = uriComponents.getHost();
        final int port = uriComponents.getPort();
        final var baseUri = STR."\{scheme}://\{host}:\{port}\{REST_PATH}";
        log.debug("getBaseUri (ohne Forwarding): baseUri={}", baseUri);
        return URI.create(baseUri);
    }

    private URI getBaseUriForwarded(final HttpServletRequest request, final String forwardedHost) {
        final var forwardedProtocol = request.getHeader(X_FORWARDED_PROTO);
        if (forwardedProtocol == null) {
            throw new IllegalStateException(STR."Kein '\{X_FORWARDED_PROTO}' im Header");
        }

        var forwardedPrefix = request.getHeader(X_FORWARDED_PREFIX);
        // x-forwarded-prefix: null bei Kubernetes Ingress Controller bzw. "/kunden" bei Spring Cloud Gateway
        if (forwardedPrefix == null) { // todo: der Spring Cloud Gateway Teil raus?
            log.trace("getBaseUriForwarded: Kein '{}' im Header", X_FORWARDED_PREFIX);
            forwardedPrefix = VERLAGE_PREFIX;
        }
        final var baseUri = STR."\{forwardedProtocol}://\{forwardedHost}\{forwardedPrefix}\{REST_PATH}";
        log.debug("getBaseUriForwarded: baseUri={}", baseUri);
        return URI.create(baseUri);
    }
}
