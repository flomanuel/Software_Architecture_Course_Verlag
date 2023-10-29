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

package com.acme.verlag.repository;

import com.acme.verlag.entity.Verlag;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.acme.verlag.repository.DB.VERLAGE;

/**
 * Repository für den Datenbankzugriff bei Verlagen.
 */
@Repository
@Slf4j
@SuppressWarnings("PublicConstructor")
public class VerlagRepository {
    /**
     * Alle Verlage als Collection ermitteln.
     *
     * @return Alle Verlage.
     */
    public @NonNull Collection<Verlag> findAll() {
        return VERLAGE;
    }

    /**
     * Einen Verlag anhand seiner ID suchen.
     *
     * @param id Die ID des gesuchten Verlages.
     * @return Optional mit dem gefundenen Verlag oder ein leeres Optional.
     */
    public Optional<Verlag> findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var result = VERLAGE.stream()
            .filter(verlag -> Objects.equals(verlag.getId(), id))
            .findFirst();
        log.debug("findById: {}", result);
        return result;
    }

    /**
     * Verlag anhand von Suchkriterien ermitteln.
     *
     * @param suchkriterien Die Suchkriterien
     * @return Die gefundenen Verlage oder eine leere Collection.
     */
    @SuppressWarnings({"ReturnCount"})
    public @NonNull Collection<Verlag> find(final Map<String, ? extends List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);
        if (suchkriterien.isEmpty()) {
            return findAll();
        }
        return emptyList();
    }
}
