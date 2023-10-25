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
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

/**
 * Anwendungslogik für Verlage.
 */
@Service
@RequiredArgsConstructor
public class VerlagReadService {
    private final VerlagRepository repo;

    /**
     * Alle Verlage suchen.
     *
     * @return Eine Liste aller gefundenen Verlage.
     */
    public Collection<Verlag> findAll() {
        return repo.findAll();
    }

    /**
     * Einen Verlag anhand seiner ID suchen.
     *
     * @param id Die ID des gesuchten Verlags.
     * @return Der gefundene Verlag.
     * @throws NotFoundException Falls kein Verlag gefunden wurde.
     */
    public @NonNull Verlag findById(final UUID id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
}
