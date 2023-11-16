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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Daten eines Verlages.
 */
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString
@SuppressWarnings({"ClassFanOutComplexity", "RequireEmptyLineBeforeBlockTagGroup"})
public class Verlag {

    /**
     * Konstante für die maximale Länge eines Verlagsnamens.
     */
    public static final int SIZE_MAX_NAME = 200;

    /**
     * Konstante für die minimale Länge eines Verlagsnamens.
     */
    public static final int SIZE_MIN_NAME = 1;

    /**
     * Die UUID des Verlags.
     */
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Der Name des Verlags.
     */
    @Size(min = SIZE_MIN_NAME, max = SIZE_MAX_NAME)
    @NotBlank
    private String name;

    /**
     * Das Gründungsdatum des Verlags.
     */
    @PastOrPresent
    @NotNull
    private LocalDate gruendungsdatum;

    /**
     * Die Adresse des Hauptsitzes des Verlags.
     */
    @ToString.Exclude
    @Valid
    @NotNull
    private Adresse hauptsitz;

    /**
     * Die in diesem Verlag erschienen Bücher.
     */
    @ToString.Exclude
    @Valid
    @UniqueElements
    private List<Buch> buecher;
}
