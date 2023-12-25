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
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
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

import static com.acme.verlag.entity.Verlag.BUCH_SUBGRAPH;
import static com.acme.verlag.entity.Verlag.HAUPTSITZ_BUECHER_GRAPH;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static java.util.Collections.emptyList;

/**
 * Daten eines Verlages.
 */
@Entity
@Table(name = "verlag")
@NamedEntityGraph(
    name = HAUPTSITZ_BUECHER_GRAPH,
    attributeNodes = {@NamedAttributeNode("hauptsitz"), @NamedAttributeNode(value = "buecher", subgraph = BUCH_SUBGRAPH)},
    subgraphs = @NamedSubgraph(
        name = BUCH_SUBGRAPH,
        attributeNodes = @NamedAttributeNode(value = "preis")
    )
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString
@SuppressWarnings({
    "ClassFanOutComplexity",
    "RequireEmptyLineBeforeBlockTagGroup",
    "DeclarationOrder",
    "JavadocDeclaration",
    "MissingSummary",
    "RedundantSuppression"})
public class Verlag {

    public interface NeuValidation {

    }

    /**
     * Konstante für die maximale Länge eines Verlagsnamens.
     */
    public static final int SIZE_MAX_NAME = 200;

    /**
     * Konstante für die minimale Länge eines Verlagsnamens.
     */
    public static final int SIZE_MIN_NAME = 1;

    /**
     * NamedEntityGraph für die Attribute "hauptsitz", "buecher", und "preis".
     */
    public static final String HAUPTSITZ_BUECHER_GRAPH = "Verlag.hauptsitzBuecher";

    /**
     * NamedSubgraph für das Attribut "preis".
     */
    public static final String BUCH_SUBGRAPH = "Buch.preis";

    /**
     * Die UUID des Verlags.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Versionsnummer für optimistische Synchronisation.
     */
    @Version
    private int version;

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
    @NotNull(groups = Verlag.NeuValidation.class)
    private Adresse hauptsitz;

    /**
     * Die in diesem Verlag erschienen Bücher.
     */
    @OneToMany(cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "verlag_id")
    @OrderColumn(name = "idx", nullable = false)
    @ToString.Exclude
    @Valid
    @UniqueElements
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
