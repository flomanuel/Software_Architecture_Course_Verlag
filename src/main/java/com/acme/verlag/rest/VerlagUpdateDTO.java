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

package com.acme.verlag.rest;

import com.acme.verlag.entity.FachbereichType;

import java.time.LocalDate;
import java.util.List;

/**
 * ValueObject für das Ändern eines neuen Verlags.
 *
 * @param name            Der Name des Verlags.
 * @param gruendungsdatum Das Gründungsdatum des Verlags.
 * @param fachbereiche    Die Fachbereiche des Verlags.
 */
public record VerlagUpdateDTO(
    String name,
    LocalDate gruendungsdatum,
    List<FachbereichType> fachbereiche) {

}
