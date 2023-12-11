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

-- https://www.postgresql.org/docs/current/sql-createtable.html
-- https://www.postgresql.org/docs/current/datatype.html
-- BEACHTE: user ist ein Schluesselwort
CREATE TABLE IF NOT EXISTS login
(
    -- https://www.postgresql.org/docs/current/datatype-uuid.html
    -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-PRIMARY-KEYS
    -- impliziter Index fuer Primary Key
    id       uuid PRIMARY KEY USING INDEX TABLESPACE verlagspace,
    -- generierte Sequenz "login_id_seq":
    -- id    integer GENERATED ALWAYS AS IDENTITY(START WITH 1000) PRIMARY KEY USING INDEX TABLESPACE verlagspace,
    username varchar(20)  NOT NULL UNIQUE USING INDEX TABLESPACE verlagspace,
    password varchar(180) NOT NULL,
    rollen   varchar(32)
) TABLESPACE verlagspace;

-- https://www.postgresql.org/docs/current/sql-createtype.html
-- https://www.postgresql.org/docs/current/datatype-enum.html
-- CREATE TYPE geschlecht AS ENUM ('MAENNLICH', 'WEIBLICH', 'DIVERS');

CREATE TABLE IF NOT EXISTS adresse
(
    id   uuid PRIMARY KEY USING INDEX TABLESPACE verlagspace,
    -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-CHECK-CONSTRAINTS
    plz  char(5)      NOT NULL CHECK (plz ~ '\d{5}'),
    ort  varchar(100) NOT NULL,
    land varchar(100) NOT NULL
) TABLESPACE verlagspace;
CREATE INDEX IF NOT EXISTS adresse_plz_idx ON adresse (plz) TABLESPACE verlagspace;

CREATE TABLE IF NOT EXISTS verlag
(
    id              uuid PRIMARY KEY USING INDEX TABLESPACE verlagspace,
    -- https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-INT
    version         integer      NOT NULL DEFAULT 0,
    name            varchar(200) NOT NULL,
    -- https://www.postgresql.org/docs/current/datatype-datetime.html
    gruendungsdatum date CHECK (verlag.gruendungsdatum <= current_date),
    hauptsitz_id    uuid         NOT NULL UNIQUE USING INDEX TABLESPACE verlagspace REFERENCES adresse,
    erzeugt         timestamp    NOT NULL,
    aktualisiert    timestamp    NOT NULL,
    -- nicht NOT NULL wegen gerichteter Beziehung und UPDATE durch Hibernate
    buch_id         uuid REFERENCES buch
) TABLESPACE verlagspace;

-- default: btree
-- https://www.postgresql.org/docs/current/sql-createindex.html
CREATE INDEX IF NOT EXISTS verlag_name_idx ON verlag (name) TABLESPACE verlagspace;

CREATE TABLE IF NOT EXISTS buch
(
    id                uuid PRIMARY KEY USING INDEX TABLESPACE verlagsname,
    isbn13            char(13)     not null,
    haupttitel        varchar(100) not null,
    nebentitel        varchar(100) not null,
    erscheinungsdatum date CHECK (buch.erscheinungsdatum <= current_date),
    auflage           int check (buch.auflage > 0),
    -- nicht NOT NULL wegen gerichteter Beziehung und UPDATE durch Hibernate
    preis_id          uuid REFERENCES preis,
    kategorie         varchar(9) check (buch.kategorie ~ 'SACHBUCH'),
    seitenzahl        int check (buch.seitenzahl > 0)
);

CREATE TABLE IF NOT EXISTS preis
(
    id           uuid PRIMARY KEY USING INDEX TABLESPACE verlagspace,
    -- https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-NUMERIC-DECIMAL
    -- https://www.postgresql.org/docs/current/datatype-money.html
    -- 10 Stellen, davon 2 Nachkommastellen
    bruttobetrag decimal(10, 2) NOT NULL,
    waehrung     char(3)        NOT NULL CHECK (waehrung ~ '[A-Z]{3}'),
    idx          integer        NOT NULL DEFAULT 0
) TABLESPACE verlagspace;
CREATE INDEX IF NOT EXISTS preis_id_idx ON buch (id) TABLESPACE verlagspace;
