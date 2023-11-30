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

import com.acme.verlag.entity.Adresse;
import com.acme.verlag.entity.Buch;
import com.acme.verlag.entity.Preis;
import com.acme.verlag.entity.Verlag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.acme.verlag.entity.KategorieType.SACHBUCH;
import static java.util.Locale.GERMANY;

/**
 * Emulation der Datenbasis für persistente Verlage.
 */
@SuppressWarnings({"UtilityClassCanBeEnum", "UtilityClass", "MagicNumber", "RedundantSuppression", "java:S1192"})
final class DB {

    /**
     * Liste der Verlage zur Emulation der DB.
     */
    @SuppressWarnings("StaticCollection")
    static final List<Verlag> VERLAGE;

    static {
        final var currencyGermany = Currency.getInstance(GERMANY);
        VERLAGE = Stream.of(
            // HTTP Put
            Verlag.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .name("Carl Hanser Verlag")
                .gruendungsdatum(LocalDate.parse("10.04.1928", DateTimeFormatter.ofPattern("dd.MM.yyy")))
                .hauptsitz(Adresse.builder()
                    .plz("81631")
                    .ort("München")
                    .land("Deutschland")
                    .build()
                )
                .buecher(Stream.of(
                        Buch.builder()
                            .haupttitel("UML 2 glasklar")
                            .nebentitel("Praxiswissen für die UML-Modellierung")
                            .isbn13("9783446430570")
                            .erscheinungsdatum(LocalDate.parse("25.05.2012", DateTimeFormatter.ofPattern("dd.MM.yyy")))
                            .auflage(4)
                            .kategorie(SACHBUCH)
                            .seitenzahl(560)
                            .preis(Preis.builder()
                                .waehrung(currencyGermany)
                                .bruttobetrag(new BigDecimal("34.9"))
                                .build()
                            )
                            .id(UUID.fromString("10000000-0000-0000-0000-000000000000"))
                            .build())
                    .collect(Collectors.toList())).build(),
            // HTTP DELETE
            Verlag.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .name("Verlagsgruppe Droemer Knaur")
                .gruendungsdatum(LocalDate.parse("02.11.1901", DateTimeFormatter.ofPattern("dd.MM.yyy")))
                .hauptsitz(Adresse.builder()
                    .plz("80636")
                    .ort("München")
                    .land("Deutschland")
                    .build()
                ).buecher(Stream.of(
                    Buch.builder()
                        .haupttitel("Die Drachen von Eden")
                        .nebentitel("Das Wunder der menschlichen Intelligenz")
                        .isbn13("9783426045992")
                        .erscheinungsdatum(LocalDate.parse("01.06.1978", DateTimeFormatter.ofPattern("dd.MM.yyy")))
                        .auflage(1)
                        .kategorie(SACHBUCH)
                        .seitenzahl(271)
                        .preis(Preis.builder()
                            .waehrung(currencyGermany)
                            .bruttobetrag(new BigDecimal("72.99")
                            ).build()
                        )
                        .id(UUID.fromString("20000000-0000-0000-0000-000000000000"))
                        .build()
                ).collect(Collectors.toList())).build(),
            // zur freien Verfügung
            Verlag.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000003"))
                .name("Rheinwerk Verlag GmbH")
                .gruendungsdatum(LocalDate.parse("09.08.1999", DateTimeFormatter.ofPattern("dd.MM.yyy")))
                .hauptsitz(Adresse.builder()
                    .plz("53227")
                    .ort("Bonn")
                    .land("Deutschland")
                    .build()
                ).buecher(Stream.of(
                    Buch.builder()
                        .haupttitel("Java ist auch eine Insel")
                        .nebentitel("Einführung, Ausbildung, Praxis")
                        .isbn13("9783836287456")
                        .erscheinungsdatum(LocalDate.parse("19.12.2022", DateTimeFormatter.ofPattern("dd.MM.yyy")))
                        .auflage(16)
                        .kategorie(SACHBUCH)
                        .seitenzahl(1258)
                        .preis(Preis.builder()
                            .waehrung(currencyGermany)
                            .bruttobetrag(new BigDecimal("49.9"))
                            .build()
                        )
                        .id(UUID.fromString("30000000-0000-0000-0000-000000000000"))
                        .build()
                ).collect(Collectors.toList())).build(),
            // HTTP Get
            Verlag.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000004"))
                .name("dtv Verlagsgesellschaft")
                .gruendungsdatum(LocalDate.parse("11.11.1960", DateTimeFormatter.ofPattern("dd.MM.yyy")))
                .hauptsitz(Adresse.builder()
                    .plz("80337")
                    .ort("München")
                    .land("Deutschland")
                    .build())
                .buecher(Stream.of(
                    Buch.builder()
                        .haupttitel("Der kleine Hobbit")
                        .nebentitel("")
                        .isbn13("9783423214124")
                        .erscheinungsdatum(LocalDate.parse("22.12.2012", DateTimeFormatter.ofPattern("dd.MM.yyy")))
                        .auflage(15)
                        .kategorie(SACHBUCH)
                        .seitenzahl(336)
                        .preis(Preis.builder()
                            .waehrung(currencyGermany)
                            .bruttobetrag(new BigDecimal("11.95"))
                            .build()
                        )
                        .id(UUID.fromString("40000000-0000-0000-0000-000000000000"))
                        .build()
                ).collect(Collectors.toList())).build()
        ).collect(Collectors.toList());
    }

    private DB() {
    }
}
