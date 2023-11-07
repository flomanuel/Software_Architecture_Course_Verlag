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

package com.acme.verlag.service;

import com.acme.verlag.entity.Verlag;
import com.acme.verlag.repository.VerlagRepository;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Anwendungslogik für Verlage.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VerlagWriteService {

    private final VerlagRepository repository;
    private final Validator validator;

    /**
     * Einen neuen Verlag anlegen.
     *
     * @param verlag Das Objekt ver neu anzulegenden Verlags. // todo: mit Genitiv-s?
     * @return Der neu angelegte Verlag mit generierter ID.
     * @throws ConstraintViolationsException Falls mindestens ein Constraint verletzt ist.
     */
    public Verlag create(final Verlag verlag) {
        log.debug("create: {}", verlag);
        final var violations = validator.validate(verlag);
        if (!violations.isEmpty()) {
            log.debug("create: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }
        final var verlagDB = repository.create(verlag);
        log.debug("create: {}", verlagDB);
        return verlagDB;
    }

    /**
     * Einen vorhandenen Verlag aktualisieren.
     *
     * @param verlag Das Objekt mit den neuen Daten. Ohne ID.
     * @param id     Die ID des zu aktualisierenden Verlags.
     * @throws ConstraintViolationsException Falls mindestens ein Constraint verletzt ist.
     * @throws NotFoundException             Kein Verlag zu gegebener ID vorhanden.
     */
    public void update(final Verlag verlag, final UUID id) {
        log.debug("update: {}", verlag);
        log.debug("update: id={}", id);
        final var violations = validator.validate(verlag);
        if (!violations.isEmpty()) {
            log.debug("update: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }

        final var verlagDBOptional = repository.findById(id);
        if (verlagDBOptional.isEmpty()) {
            throw new NotFoundException(id);
        }

        verlag.setId(id);
        repository.update(verlag);
    }
}
