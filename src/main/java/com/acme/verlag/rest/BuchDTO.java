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

import com.acme.verlag.entity.KategorieType;

import java.time.LocalDate;
import java.util.UUID;

/**
 * ValueObject für das Neuanlegen und Ändern eines Buches.
 *
 * @param haupttitel        Der Haupttitel eins Buches.
 * @param nebentitel        Der Nebentitel eines Buches.
 * @param erscheinungsdatum Das Erscheinungsdatum eines Buches.
 * @param auflage           Die Auflage eines Buches.
 * @param preis             Der Preis eines Buches.
 * @param kategorie         Die Kategorie eines Buches.
 * @param isbn13            Die ISBN13 eines Buches.
 * @param seitenzahl        Die Seitenzahl eines Buches.
 * @param autorVorname      Der Vorname des Autors.
 * @param autorNachname     Der Nachname des Autors.
 * @param autorId           Die ID des Autors.
 */
record BuchDTO(
    String haupttitel,
    String nebentitel,
    LocalDate erscheinungsdatum,
    int auflage,
    PreisDTO preis,
    KategorieType kategorie,
    String isbn13,
    int seitenzahl,
    String autorVorname,
    String autorNachname,
    UUID autorId) {

}
