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

package com.acme.verlag.graphql;

import com.acme.verlag.entity.KategorieType;
import com.acme.verlag.entity.Preis;

/**
 * Eine Value-Klasse für Eingabedaten passend zu BuchInput aus dem GraphQL-Schema.
 *
 * @param isbn13           ISBN-13
 * @param haupttitel       Haupttitel
 * @param nebentitel       Nebentitel
 * @param erscheinungsjahr Erscheinungsjahr
 * @param auflage          Auflage
 * @param preis            Preis
 * @param kategorie        Kategorie
 * @param seitenzahl       Seitenzahl
 */
record BuchInput(String isbn13,
                 String haupttitel,
                 String nebentitel,
                 String erscheinungsjahr,
                 int auflage,
                 Preis preis,
                 KategorieType kategorie,
                 int seitenzahl
) {

}