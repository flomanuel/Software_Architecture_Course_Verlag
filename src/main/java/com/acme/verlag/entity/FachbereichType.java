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

package com.acme.verlag.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FachbereichType {
    /**
     * _Geschichtswissenschaft_ mit dem internen Wert `S`.
     */
    GESCHICHTSWISSENSCHAFT("G"),

    /**
     * _Physik_ mit dem internen Wert `S`.
     */
    PHYSIK("P"),

    /**
     * _Chemie_ mit dem internen Wert `S`.
     */
    CHEMIE("C"),

    /**
     * _Wirtschaft_ mit dem internen Wert `S`.
     */
    WIRTSCHAFT("W"),

    /**
     * _Informatik_ mit dem internen Wert `S`.
     */
    INFORMATIK("I");

    private final String value;

    FachbereichType(final String value) {
        this.value = value;
    }

    /**
     * Einen enum-Wert als String mit dem internen Wert ausgeben.
     * Dieser Wert wird durch Jackson in einem JSON-Datensatz verwendet.
     *
     * @return Interner Wert.
     */
    @JsonValue
    @Override
    public String toString() {
        return value;
    }
}
