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
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
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
import org.hibernate.validator.constraints.ISBN;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

/**
 * Daten eines Buches für die Anwendungslogik und zum Abspeichern in der Datenbank.
 * <p/>
 * <img src="../../../../../asciidoc/Verlag.svg" alt="Klassendiagramm">
 */
@Entity
@Table(name = "buch")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuppressWarnings({"RequireEmptyLineBeforeBlockTagGroup", "ClassFanOutComplexity"})
public class Buch {

    /**
     * Konstante für die maximale Länge eines Haupttitels.
     */
    public static final int SIZE_MAX_HAUPTTITEL = 100;

    /**
     * Konstante für die minimale Länge eines Haupttitels.
     */
    public static final int SIZE_MIN_HAUPTTITEL = 1;

    /**
     * Konstante für die maximale Länge eines Nebentitels.
     */
    public static final int SIZE_MAX_NEBENTITEL = 100;

    /**
     * Die UUID des Buches.
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
     * Die ID des Autors, der dieses Buch verfasst hat.
     */
    @NotNull
    // Der Spaltenwert referenziert einen Wert aus einer anderen DB.
    @Column(name = "autor_id")
    private UUID autorId;

    /**
     * Die ISBN-13 des Buches.
     */
    @EqualsAndHashCode.Include
    @ISBN
    private String isbn13;

    /**
     * Der Haupttitel des Buches.
     */
    @Size(min = SIZE_MIN_HAUPTTITEL, max = SIZE_MAX_HAUPTTITEL)
    @NotBlank
    private String haupttitel;

    /**
     * Der Nebentitel des Buches.
     */
    @Size(max = SIZE_MAX_NEBENTITEL)
    @NotNull
    private String nebentitel;

    /**
     * Die Auflage des Buches.
     */
    @Positive
    private int auflage;

    /**
     * Die Seitenzahl des Buches.
     */
    @Positive
    private int seitenzahl;

    /**
     * Das Erscheinungsdatum des Buches.
     */
    @PastOrPresent
    @NotNull
    private LocalDate erscheinungsdatum;

    /**
     * Die thematische Kategorie des Buches.
     */
    @Enumerated(STRING)
    @NotNull
    private KategorieType kategorie;

    /**
     * Der Vorname des Autors, der dieses Buch verfasst hat.
     */
    @Transient
    private String autorVorname;

    /**
     * Der Nachname des Autors, der dieses Buch verfasst hat.
     */
    @Transient
    private String autorNachname;

    /**
     * Der Preis des Buches.
     */
    @OneToOne(optional = false, cascade = {PERSIST, REMOVE}, fetch = LAZY, orphanRemoval = true)
    @Valid
    @NotNull
    private Preis preis;
}
