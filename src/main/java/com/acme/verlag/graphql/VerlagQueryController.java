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

import com.acme.verlag.entity.Buch;
import com.acme.verlag.entity.Verlag;
import com.acme.verlag.service.VerlagReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyMap;

/**
 * Eine Controller-Klasse für das Lesen mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class VerlagQueryController {

    private final VerlagReadService service;

    /**
     * Suche anhand der Verlag-ID.
     *
     * @param id ID des zu suchenden Verlags
     * @return Der gefundene Verlag
     */
    @QueryMapping("verlag")
    Verlag findById(@Argument final UUID id) {
        log.debug("findById: id={}", id);
        final var verlag = service.findById(id);
        log.debug("findById: verlag={}", verlag);
        return verlag;
    }

    /**
     * Suche mit verschiedenen Suchkriterien.
     *
     * @param input Suchkriterien und ihre zugehörigen Werte
     * @return Die gefundenen Verlage als Collection
     */
    @QueryMapping("verlage")
    Collection<Verlag> find(@Argument final Optional<Suchkriterien> input) {
        log.debug("find: suchkriterien={}", input);
        final var suchkriterien = input.map(Suchkriterien::toMap).orElse(emptyMap());
        final var verlage = service.find(suchkriterien);
        log.debug("find: verlage={}", verlage);
        return verlage;
    }

    /**
     * Handler-Methode für das GraphQL-Field "buecher" für den GraphQL-Type "Verlag".
     *
     * @param verlag Injiziertes Objekt vom GraphQL-Type "Verlag", zu dem diese Handler-Methode aufgerufen wird.
     * @param first  Die ersten Buchdaten in der Liste der Bücher zum gefundenen Verlag.
     * @return Liste mit Buch-Objekten, die für das Field "buecher" beim gefundenen Verlag-Objekt verwendet werden.
     */
    List<Buch> buecher(final Verlag verlag, @Argument final int first) {
        log.debug("buecher: verlag={}, buecher={}, first={}", verlag, verlag.getBuecher(), first);
        if (first <= 0) {
            return List.of();
        }
        final var anzahlBuecher = verlag.getBuecher().size();
        final var end = first <= anzahlBuecher ? first : anzahlBuecher;
        return verlag.getBuecher().subList(0, end);
    }
}
