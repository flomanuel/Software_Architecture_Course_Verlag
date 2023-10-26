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

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Year;

/**
 * Daten eines Buches für die Anwendungslogik und zum Abspeichern in der Datenbank.
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuppressWarnings({"RequireEmptyLineBeforeBlockTagGroup"})
public class Buch {

    /**
     * Muster für eine gültige ISBN-13.
     */
    public static final String ISBN13_PATTERN = "[0-9]{13}";

    /**
     * Der Haupttitel des Buches.
     */
    @NotNull
    private String haupttitel;

    /**
     * Der Nebentitel des Buches.
     */
    @NotNull
    private String nebentitel;

    /**
     * Das Erscheinungsjahr des Buches.
     */
    @NotNull
    @PastOrPresent
    private Year erscheinungsjahr;

    /**
     * Die Auflage des Buches.
     */
    private int auflage;

    /**
     * Der Preis des Buches.
     */
    @ToString.Exclude
    @NotNull
    private com.acme.verlag.entity.Preis preis;

    /**
     * Die thematische Kategorie des Buches.
     */
    private com.acme.verlag.entity.KategorieType kategorie;

    /**
     * Die ISBN-13 des Buches.
     */
    @NotNull
    @EqualsAndHashCode.Include
    @Pattern(regexp = ISBN13_PATTERN)
    private String isbn13;

    /**
     * Die Seitenzahl des Buches.
     */
    private int seitenzahl;
}
