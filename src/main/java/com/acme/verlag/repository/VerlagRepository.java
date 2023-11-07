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

import static com.acme.verlag.repository.DB.VERLAGE;

import com.acme.verlag.entity.Verlag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static java.util.Collections.emptyList;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.time.Year;
import java.util.stream.IntStream;


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
    public Collection<Verlag> findAll() {
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
    public Collection<Verlag> find(final Map<String, ? extends List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return findAll();
        }

        for (final var entry : suchkriterien.entrySet()) {
            switch (entry.getKey()) {
                case "name" -> {
                    return findByName(entry.getValue().getFirst());
                }
                case "gruendungsjahr" -> {
                    return findByGruendungsjahr(entry.getValue().getFirst());
                }
                default -> {
                    log.debug("find: ungueltiges Suchkriterium={}", entry.getKey());
                    return emptyList();
                }
            }
        }
        return emptyList();
    }

    /**
     * Verlage anhand des (Teil-) Verlagsnamens suchen.
     *
     * @param name Der (Teil-) Verlagsname der gesuchten Verlage.
     * @return Die gefundenen Verlage oder eine leere Collection.
     */
    private Collection<Verlag> findByName(final CharSequence name) {
        log.debug("findByName: Name={}", name);
        final var verlage = VERLAGE.stream()
            .filter(verlag -> verlag.getName().contains(name))
            .toList();
        log.debug("findByName: verlage={}", verlage);
        return verlage;
    }

    /**
     * Verlage anhand des Gründungsjahres suchen.
     *
     * @param gruendungsjahr Das Gründungsjahr der gesuchten Verlage.
     * @return Die gefundenen Verlage oder eine leere Collection.
     */
    private Collection<Verlag> findByGruendungsjahr(final CharSequence gruendungsjahr) {
        log.debug("findByGruendungsjahr: Gruendungsjahr={}", gruendungsjahr);
        final var verlage = VERLAGE.stream()
            .filter(verlag -> verlag.getGruendungsjahr().compareTo(Year.parse(gruendungsjahr)) == 0)
            .toList();
        log.debug("findByGruendungsjahr: verlage={}", verlage);
        return verlage;
    }

    /**
     * Einen neuen Verlag anlegen.
     *
     * @param verlag Das Objekt des neu anzulegenden Verlags.
     * @return Der neu angelegte Verlag mit generierter ID.
     */
    public Verlag create(final Verlag verlag) {
        log.debug("create: {}", verlag);
        verlag.setId(UUID.randomUUID());
        VERLAGE.add(verlag);
        log.debug("create: {}", verlag);
        return verlag;
    }

    /**
     * Einen vorhandenen Verlag aktualisieren.
     *
     * @param verlag Das Verlags-Objekt mit den neuen Daten.
     */
    public void update(final Verlag verlag) {
        log.debug("update: {}", verlag);
        final OptionalInt index = IntStream
            .range(0, VERLAGE.size())
            .filter(i -> Objects.equals(VERLAGE.get(i).getId(), verlag.getId()))
            .findFirst();
        log.trace("update: index={}", index);
        if (index.isEmpty()) {
            return;
        }
        VERLAGE.set(index.getAsInt(), verlag);
        log.debug("update: {}", verlag);
    }
}
