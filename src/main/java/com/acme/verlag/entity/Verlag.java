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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static java.util.Collections.emptyList;

/**
 * Daten eines Verlages.
 */
@Entity
@Table(name = "verlag")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString
@SuppressWarnings({"ClassFanOutComplexity", "RequireEmptyLineBeforeBlockTagGroup"})
public class Verlag {

    /**
     * NamedEntityGraph für das Attribut "fachbereiche".
     */
    public static final String FACHBEREICHE_GRAPH = "Verlag.fachbereiche";

    /**
     * NamedEntityGraph für das Attribut "hauptsitz".
     */
    public static final String HAUPTSITZ_GRAPH = "Verlag.hauptsitz";

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
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @CreationTimestamp
    private LocalDateTime erzeugt;

    @UpdateTimestamp
    private LocalDateTime aktualisiert;

    /**
     * Der Name des Verlags.
     */
    @Size(min = SIZE_MIN_NAME, max = SIZE_MAX_NAME)
    @NotBlank
    private String name;

    /**
     * Die Fachbereiche des Verlags.
     */
    @Transient
    @UniqueElements
    private List<FachbereichType> fachbereiche;

    @Column(name = "fachbereiche")
    private String fachbereicheStr;

    /**
     * Das Gründungsdatum des Verlags.
     */
    @PastOrPresent
    @NotNull
    private LocalDate gruendungsdatum;

    /**
     * Die Adresse des Hauptsitzes des Verlags.
     */
    @OneToOne(optional = false, cascade = {PERSIST, REMOVE}, fetch = LAZY, orphanRemoval = true)
    @ToString.Exclude
    @Valid
    @NotNull
    private Adresse hauptsitz;

    /**
     * Die in diesem Verlag erschienen Bücher.
     */
    @OneToMany(cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "buch_id")
    @OrderColumn(name = "idx")
    @ToString.Exclude
    @Valid
    @UniqueElements
    @Transient
    private List<Buch> buecher;

    /**
     * Verlagsdaten überschreiben.
     *
     * @param verlag Neue Verlagsdaten.
     */
    public void set(final Verlag verlag) {
        name = verlag.name;
        fachbereiche = verlag.fachbereiche;
        gruendungsdatum = verlag.gruendungsdatum;
        hauptsitz = verlag.hauptsitz;
        buecher = verlag.buecher;
    }

    @PrePersist
    private void buildFachbereicheStr() {
        if (fachbereiche == null || fachbereiche.isEmpty()) {
            // NULL in der DB-Spalte
            fachbereicheStr = null;
            return;
        }
        final var stringList = fachbereiche.stream()
            .map(Enum::name)
            .toList();
        fachbereicheStr = String.join(",", stringList);
    }

    @PostLoad
    private void loadFachbereiche() {
        if (fachbereicheStr == null) {
            // NULL in der DB-Spalte
            fachbereiche = emptyList();
            return;
        }
        final var interessenArray = fachbereicheStr.split(",");
        fachbereiche = Arrays.stream(interessenArray)
            .map(FachbereichType::valueOf)
            .collect(Collectors.toList());
    }
}
