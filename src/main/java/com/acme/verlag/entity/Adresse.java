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

package com.acme.verlag.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Adressdaten für die Anwendungslogik und zum Abspeichern in der Datenbank.
 */
@Builder
@Getter
@Setter
@ToString
@SuppressWarnings({"RequireEmptyLineBeforeBlockTagGroup"})
public class Adresse {

    /**
     * Konstante für die maximale Länge eines Ortsnamen.
     */
    public static final int SIZE_MAX_ORTSNAME = 100;

    /**
     * Konstante für die minimale Länge eines Ortsnamen.
     */
    public static final int SIZE_MIN_ORTSNAME = 1;

    /**
     * Konstante für die maximale Länge eines Ländernamens.
     */
    public static final int SIZE_MAX_LAENDERNAME = 100;

    /**
     * Konstante für die minimale Länge eines Ländernamens.
     */
    public static final int SIZE_MIN_LAENDERNAME = 1;

    /**
     * Konstante für den regulären Ausdruck einer Postleitzahl als 5-stellige Zahl mit führender Null.
     */
    public static final String PLZ_PATTERN = "^\\d{5}$";

    /**
     * Postleitzahl der Adresse.
     */
    @NotNull
    @Pattern(regexp = PLZ_PATTERN)
    private String plz;

    /**
     * Ortsname der Adresse.
     */
    @Size(min = SIZE_MIN_ORTSNAME, max = SIZE_MAX_ORTSNAME)
    @NotBlank
    private String ort;

    /**
     * Ländername der Adresse.
     */
    @Size(min = SIZE_MIN_LAENDERNAME, max = SIZE_MAX_LAENDERNAME)
    @NotBlank
    private String land;
}
