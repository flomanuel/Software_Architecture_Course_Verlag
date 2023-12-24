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
--  docker compose exec postgres bash
--  psql --dbname=verlag --username=verlag [--file=/sql/V1.1__Insert.sql]

-- COPY mit CSV-Dateien erfordert den Pfad src/main/resources/...
-- Dieser Pfad existiert aber nicht im Docker-Image
-- https://www.postgresql.org/docs/current/sql-copy.html

INSERT INTO login (id, username, password, rollen)
VALUES ('30000000-0000-0000-0000-000000000000', 'admin',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g',
        'ADMIN,KUNDE,ACTUATOR'),
       ('30000000-0000-0000-0000-000000000001', 'alpha',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g',
        'KUNDE'),
       ('30000000-0000-0000-0000-000000000020', 'alpha2',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g',
        'KUNDE'),
       ('30000000-0000-0000-0000-000000000030', 'alpha3',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g',
        'KUNDE'),
       ('30000000-0000-0000-0000-000000000040', 'delta',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g',
        'KUNDE'),
       ('30000000-0000-0000-0000-000000000050', 'epsilon',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g',
        'KUNDE'),
       ('30000000-0000-0000-0000-000000000060', 'phi',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g',
        'KUNDE');

INSERT INTO adresse (id, plz, ort, land)
VALUES ('00000000-0000-0000-0000-000000000000', '81631', 'München', 'Deutschland'),
       ('00000000-0000-0000-0000-000000000001', '80636', 'München', 'Deutschland'),
       ('00000000-0000-0000-0000-000000000002', '53227', 'Bonn', 'Deutschland'),
       ('00000000-0000-0000-0000-000000000003', '80337', 'München', 'Deutschland');

INSERT INTO preis (id, bruttobetrag, waehrung)
VALUES ('00000000-0000-0000-0000-000000000000', '34.9', 'EUR'),
       ('00000000-0000-0000-0000-000000000001', '72.99', 'EUR'),
       ('00000000-0000-0000-0000-000000000002', '49.9', 'EUR'),
       ('00000000-0000-0000-0000-000000000003', '11.95', 'EUR');

INSERT INTO verlag (id, version, name, gruendungsdatum, hauptsitz_id, fachbereiche, erzeugt, aktualisiert)
VALUES ('00000000-0000-0000-0000-000000000001', 0, 'Carl Hanser Verlag', '1928-04-10',
        '00000000-0000-0000-0000-000000000000', 'INFORMATIK',
        '2021-01-01 12:00:00', '2021-01-01 12:00:00'),
       ('00000000-0000-0000-0000-000000000002', 0, 'Verlagsgruppe Droemer Knaur', '1901-11-02',
        '00000000-0000-0000-0000-000000000001', 'PHYSIK',
        '2021-01-02 12:00:00', '2021-01-02 12:00:00'),
       ('00000000-0000-0000-0000-000000000003', 0, 'Rheinwerk Verlag GmbH', '1999-08-09',
        '00000000-0000-0000-0000-000000000002', 'INFORMATIK',
        '2021-01-03 12:00:00', '2021-01-03 12:00:00'),
       ('00000000-0000-0000-0000-000000000004', 0, 'dtv Verlagsgesellschaft', '1960-11-11',
        '00000000-0000-0000-0000-000000000003', 'FANTASY',
        '2021-01-04 12:00:00', '2021-01-04 12:00:00');

INSERT INTO buch (id, isbn13, haupttitel, nebentitel, erscheinungsdatum, auflage, preis_id, kategorie, seitenzahl, idx,
                  verlag_id, erzeugt, aktualisiert, autor_id)
VALUES ('10000000-0000-0000-0000-000000000000', '9783446430570', 'UML 2 glasklar',
        'Praxiswissen für die UML-Modellierung', '2012-05-25', 4,
        '00000000-0000-0000-0000-000000000000', 'SACHBUCH', 560, 0, '00000000-0000-0000-0000-000000000001',
        '2022-01-01 12:00:00', '2022-01-01 12:00:00', '00000000-0000-0000-0000-000000000001'),
       ('20000000-0000-0000-0000-000000000000', '9783426045992', 'Die Drachen von Eden',
        'Das Wunder der menschlichen Intelligenz', '1978-06-01', 1,
        '00000000-0000-0000-0000-000000000001', 'SACHBUCH', 271, 0, '00000000-0000-0000-0000-000000000002',
        '2022-01-02 12:00:00', '2022-01-02 12:00:00', '00000000-0000-0000-0000-000000000002'),
       ('30000000-0000-0000-0000-000000000000', '9783836287456', 'Java ist auch eine Insel',
        'Einführung, Ausbildung, Praxis', '2022-12-19', 16,
        '00000000-0000-0000-0000-000000000002', 'SACHBUCH', 1258, 0, '00000000-0000-0000-0000-000000000003',
        '2022-01-03 12:00:00', '2022-01-03 12:00:00', '00000000-0000-0000-0000-000000000003'),
       ('40000000-0000-0000-0000-000000000000', '9783423214124', 'Der kleine Hobbit',
        '', '2012-12-22', 15,
        '00000000-0000-0000-0000-000000000003', 'SACHBUCH', 336, 0, '00000000-0000-0000-0000-000000000004',
        '2022-01-04 12:00:00', '2022-01-04 12:00:00', '00000000-0000-0000-0000-000000000003');
