/*
 * Copyright (c) 2023 - present Florian Sauer
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.acme.verlag.rest;

import com.acme.verlag.service.VerlagReadService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.acme.verlag.rest.VerlagGetController.REST_PATH;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

/**
 * Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden.
 * <p/>
 * <img src="../../../../../asciidoc/VerlagGetController.svg" alt="Klassendiagramm">
 */
@RestController
@RequestMapping(REST_PATH)
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(title = "Verlag API", version = "v1"))
@Slf4j
@SuppressWarnings("java:S1075")
public class VerlagGetController {

    /**
     * Basispfad für die REST-Schnittstelle.
     */
    public static final String REST_PATH = "/rest";

    /**
     * Muster für eine UUID.
     */
    public static final String ID_PATTERN =
        "[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}";

    /**
     * Service für Verlage.
     */
    private final VerlagReadService service;
    private final UriHelper uriHelper;

    /**
     * Suche anhand der Verlags-ID als Pfad-Parameter.
     *
     * @param id      Die ID des zu suchenden Verlags.
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Der gefundene Verlag mit Atom-Links.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit der Verlag-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Verlag gefunden")
    @ApiResponse(responseCode = "404", description = "Verlag nicht gefunden")
    ResponseEntity<VerlagModel> getById(@PathVariable final UUID id,
                                        final HttpServletRequest request,
                                        @RequestHeader("If-None-Match") final Optional<String> version
    ) {
        log.debug("getById: id={}, version={}", id, version);
        // Geschäftslogik
        final var verlag = service.findById(id);
        final var currentVersion = STR."\"\{verlag.getVersion()}\"";
        if (Objects.equals(version.orElse(null), currentVersion)) {
            return status(NOT_MODIFIED).build();
        }
        // HATEOAS
        final var model = new VerlagModel(verlag);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = STR."\{baseUri}/\{verlag.getId()}}";
        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        model.add(selfLink, listLink, addLink, updateLink);
        log.debug("getById: {}", verlag);
        return ok().eTag(currentVersion).body(model);
    }

    /**
     * Suche mit diversen Suchkriterien als Query-Parameter.
     *
     * @param suchkriterien Die Query-Parameter als Map.
     * @param request       Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Alle gefundenen Verlage als CollectionModel.
     */
    @GetMapping(produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "CollectionModel mit den Verlagen")
    @ApiResponse(responseCode = "404", description = "Keine Verlage gefunden")
    CollectionModel<VerlagModel> get(
        @RequestParam final MultiValueMap<String, String> suchkriterien, final HttpServletRequest request
    ) {
        log.debug("get: suchkriterien={}", suchkriterien);
        // HATEOAS
        final var baseUri = uriHelper.getBaseUri(request).toString();
        // Geschäftslogik und HATEOAS
        final var models = service.find(suchkriterien).stream()
            .map(verlag -> {
                final var model = new VerlagModel(verlag);
                model.add(Link.of(STR."\{baseUri}/\{verlag.getId()}"));
                return model;
            })
            .toList();
        log.debug("get: {}", models);
        return CollectionModel.of(models);
    }
}
