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
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.acme.verlag.repository.DB.VERLAGE;

/**
 * Repository für den Datenbankzugriff bei Verlagen.
 */
@Repository
public class VerlagRepository {
    /**
     * Alle Verlage als Collection ermitteln.
     * @return Alle Verlage
     */
    public Collection<Verlag> findAll() {
        return VERLAGE;
    }

    /**
     *
     * @param id Die ID des gesuchten Verlages.
     * @return Optional mit dem gefundenen Verlag oder ein leeres Optional.
     */
    public Optional<Verlag> findById(final UUID id) {
        return VERLAGE.stream()
            .filter(verlag -> Objects.equals(verlag.getId(), id))
            .findFirst();
    }
}
