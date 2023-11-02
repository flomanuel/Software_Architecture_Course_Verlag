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

package com.acme.verlag.service;

import com.acme.verlag.entity.Verlag;
import com.acme.verlag.repository.VerlagRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Anwendungslogik für Verlage.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VerlagReadService {

    private final VerlagRepository repo;

    /**
     * Einen Verlag anhand seiner ID suchen.
     *
     * @param id Die ID des gesuchten Verlags.
     * @return Der gefundene Verlag.
     * @throws NotFoundException Falls kein Verlag gefunden wurde.
     */
    public @NonNull Verlag findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var verlag = repo.findById(id).orElseThrow(() -> new NotFoundException(id));
        log.debug("findById: {}", verlag);

        return verlag;
    }

    /**
     * Verlage anhand von Suchkriterien als Collection suchen.
     * Gibt es keine Suchkriterien, werden alle Verlage gesucht.
     *
     * @param suchkriterien Die Suchkriterien
     * @return Die gefundenen Verlage oder eine leere Liste.
     */
    @SuppressWarnings({"ReturnCount", "NestedIfDepth"})
    public @NonNull Collection<Verlag> find(@NonNull final Map<String, List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return repo.findAll();
        }

        final var verlage = repo.find(suchkriterien);
        if (verlage.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        log.debug("find: {}", verlage);

        return verlage;
    }
}
