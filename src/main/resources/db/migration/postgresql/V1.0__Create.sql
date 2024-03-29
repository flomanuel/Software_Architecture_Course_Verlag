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
-- docker compose exec postgres bash
-- psql --dbname=verlag --username=verlag [--file=/sql/V1.0__Create.sql]

-- BEACHTE: user ist ein Schluesselwort
CREATE TABLE IF NOT EXISTS login
(
    id       uuid PRIMARY KEY USING INDEX TABLESPACE verlagspace,
    username varchar(20)  NOT NULL UNIQUE USING INDEX TABLESPACE verlagspace,
    password varchar(180) NOT NULL,
    rollen   varchar(32)
) TABLESPACE verlagspace;

CREATE TABLE IF NOT EXISTS adresse
(
    id   uuid PRIMARY KEY USING INDEX TABLESPACE verlagspace,
    plz  char(5)      NOT NULL CHECK (plz ~ '\d{5}'),
    ort  varchar(100) NOT NULL,
    land varchar(100) NOT NULL
) TABLESPACE verlagspace;
CREATE INDEX IF NOT EXISTS adresse_plz_idx ON adresse (plz) TABLESPACE verlagspace;

CREATE TABLE IF NOT EXISTS verlag
(
    id              uuid PRIMARY KEY USING INDEX TABLESPACE verlagspace,
    version         integer      NOT NULL DEFAULT 0,
    name            varchar(200) NOT NULL,
    gruendungsdatum date         NOT NULL CHECK (verlag.gruendungsdatum <= current_date),
    hauptsitz_id    uuid         NOT NULL UNIQUE USING INDEX TABLESPACE verlagspace REFERENCES adresse,
    fachbereiche    varchar(32)  NOT NULL CHECK (verlag.fachbereiche ~
                                                 'GESCHICHTSWISSENSCHAFT|PHYSIK|CHEMIE|WIRTSCHAFT|INFORMATIK|FANTASY'),
    erzeugt         timestamp    NOT NULL,
    aktualisiert    timestamp    NOT NULL
) TABLESPACE verlagspace;

CREATE INDEX IF NOT EXISTS verlag_name_idx ON verlag (name) TABLESPACE verlagspace;

CREATE TABLE IF NOT EXISTS preis
(
    id           uuid PRIMARY KEY USING INDEX TABLESPACE verlagspace,
    bruttobetrag decimal(10, 2) NOT NULL,
    waehrung     char(3)        NOT NULL CHECK (waehrung ~ '[A-Z]{3}')
) TABLESPACE verlagspace;

CREATE TABLE IF NOT EXISTS buch
(
    id                uuid PRIMARY KEY USING INDEX TABLESPACE verlagspace,
    isbn13            char(13)     not null,
    haupttitel        varchar(100) not null,
    nebentitel        varchar(100) not null,
    erscheinungsdatum date         NOT NULL CHECK (buch.erscheinungsdatum <= current_date),
    auflage           int          not null check (buch.auflage > 0),
    preis_id          uuid         NOT NULL REFERENCES preis,
    kategorie         varchar(40)  NOT NULL check (buch.kategorie ~ 'SACHBUCH'),
    seitenzahl        int          NOT NULL check (buch.seitenzahl > 0),
    idx               integer      NOT NULL DEFAULT 0,
    verlag_id         uuid REFERENCES verlag,
    autor_id          uuid         NOT NULL
);

CREATE INDEX IF NOT EXISTS buch_verlag_id_idx ON buch (verlag_id) TABLESPACE verlagspace;
