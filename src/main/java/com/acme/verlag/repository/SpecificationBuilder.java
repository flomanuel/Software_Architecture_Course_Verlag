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

package com.acme.verlag.repository;

import com.acme.verlag.entity.Adresse_;
import com.acme.verlag.entity.FachbereichType;
import com.acme.verlag.entity.Verlag;
import com.acme.verlag.entity.Verlag_;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Singleton-Klasse, um Specifications für Queries in Spring Data JPA zu bauen.
 */
@Component
@Slf4j
public class SpecificationBuilder {

    public Optional<Specification<Verlag>> build(final Map<String, ? extends List<String>> queryParams) {
        log.debug("build: queryParams={}", queryParams);
        if (queryParams.isEmpty()) {
            return Optional.empty();
        }
        final var specs = queryParams.entrySet().stream()
            .map(this::toSpecification).
            toList();
        if (specs.isEmpty() || specs.contains(null)) {
            return Optional.empty();
        }
        return Optional.of(Specification.allOf(specs));
    }

    private Specification<Verlag> toSpecification(Map.Entry<String, ? extends List<String>> entry) {
        log.trace("toSpec: entry={}", entry);
        final var key = entry.getKey();
        final var values = entry.getValue();
        if ("fachbereiche".contentEquals(key)) {
            return toSpecificationFachbereiche(values);
        }
        if (values == null || values.size() != 1) {
            return null;
        }
        final var value = values.getFirst();
        return switch (key) {
            case "name" -> name(value);
            case "ort" -> ort(value);
            case "plz" -> plz(value);
            case "land" -> land(value);
            case "gruendungsdatum" -> gruendungsdatum(value);
            default -> null;
        };
    }

    private Specification<Verlag> toSpecificationFachbereiche(final Collection<String> fachbereiche) {
        log.trace("build: fachbereiche={}", fachbereiche);
        if (fachbereiche == null || fachbereiche.isEmpty()) {
            return null;
        }
        final var specsImmutable = fachbereiche.stream()
            .map(this::fachbereich)
            .toList();
        if (specsImmutable.isEmpty() || specsImmutable.contains(null)) {
            return null;
        }
        final List<Specification<Verlag>> specs = new ArrayList<>(specsImmutable);
        final var first = specs.removeFirst();
        return specs.stream().reduce(first, Specification::and);
    }

    private Specification<Verlag> name(final String name) {
        return (root, query, builder) ->
            builder.like(builder.lower(root.get(Verlag_.name)),
                builder.lower(builder.literal(STR."%\{name}%")));
    }

    private Specification<Verlag> gruendungsdatum(final String gruendungsdatum) {
        return (root, query, builder) ->
            builder.equal(root.get(Verlag_.gruendungsdatum), LocalDate.parse(gruendungsdatum));
    }

    private Specification<Verlag> fachbereich(final String fachbereich) {
        final var fachbereichEnum = FachbereichType.of(fachbereich);
        if (fachbereichEnum == null) {
            return null;
        }
        return (root, query, builder) -> builder.like(
            root.get(Verlag_.fachbereicheStr),
            builder.literal(STR."%\{fachbereichEnum.name()}%")
        );
    }

    private Specification<Verlag> ort(final String ort) {
        return (root, query, builder) -> builder.like(
            builder.lower(root.get(Verlag_.hauptsitz).get(Adresse_.ort)),
            builder.lower(builder.literal(STR."\{ort}%")));
    }

    private Specification<Verlag> plz(final String plz) {
        return (root, query, builder) -> builder.like(
            root.get(Verlag_.hauptsitz).get(Adresse_.plz),
            STR."\{plz}%"
        );
    }

    private Specification<Verlag> land(final String land) {
        return (root, query, builder) -> builder.like(
            builder.lower(root.get(Verlag_.hauptsitz).get(Adresse_.land)),
            builder.lower(builder.literal(STR."\{land}%")));
    }
}
