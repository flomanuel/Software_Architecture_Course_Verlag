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

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

/**
 * Währung und Bruttobetrag für den Buchpreis.
 * <p/>
 * <img src="../../../../../asciidoc/Verlag.svg" alt="Klassendiagramm">
 */
@Entity
@Table(name = "preis")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@SuppressWarnings({"RequireEmptyLineBeforeBlockTagGroup"})
public class Preis {

    /**
     * Konstante für die minimale Größe eines Bruttobetrags.
     */
    public static final String DECIMAL_MIN_BRUTTOBETRAG = "0.0";

    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Der Bruttobetrag des Preises.
     */
    @DecimalMin(DECIMAL_MIN_BRUTTOBETRAG)
    @NotNull
    private BigDecimal bruttobetrag;

    /**
     * Die Währung des Preises.
     */
    @NotNull
    private Currency waehrung;
}
