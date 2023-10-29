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

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * RuntimeException, falls kein Verlag gefunden wurde.
 */
@Getter
public final class NotFoundException extends RuntimeException {
    /**
     * Die fehlende ID.
     */
    private final UUID id;

    /**
     * Die übergebenen Suchkriterien.
     */
    private final Map<String, List<String>> suchkriterien;

    NotFoundException(final UUID id) {
        super(STR."Kein Verlag mit der ID \{id} gefunden.");
        this.id = id;
        this.suchkriterien = null;
    }

    NotFoundException(final Map<String, List<String>> suchkriterien) {
        super("Keine Verlage gefunden.");
        this.suchkriterien = suchkriterien;
        this.id = null;
    }
}
