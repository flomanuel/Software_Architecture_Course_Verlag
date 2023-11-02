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
@SuppressWarnings({"RequireEmptyLineBeforeBlockTagGroup", "ClassFanOutComplexity"})
public class Buch {

    /**
     * Der Haupttitel des Buches.
     */
    private String haupttitel;

    /**
     * Der Nebentitel des Buches.
     */
    private String nebentitel;

    /**
     * Das Erscheinungsjahr des Buches.
     */
    private Year erscheinungsjahr;

    /**
     * Die Auflage des Buches.
     */
    private int auflage;

    /**
     * Der Preis des Buches.
     */
    private Preis preis;

    /**
     * Die thematische Kategorie des Buches.
     */
    private KategorieType kategorie;

    /**
     * Die ISBN-13 des Buches.
     */
    @EqualsAndHashCode.Include
    private String isbn13;

    /**
     * Die Seitenzahl des Buches.
     */
    private int seitenzahl;
}
