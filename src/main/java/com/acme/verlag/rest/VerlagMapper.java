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

package com.acme.verlag.rest;

import com.acme.verlag.entity.Adresse;
import com.acme.verlag.entity.Buch;
import com.acme.verlag.entity.Preis;
import com.acme.verlag.entity.Verlag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

/**
 * Mapper zwischen Entity-Klassen.
 * Siehe build\generated\sources\annotationProcessor\java\main\...\VerlagMapperImpl.java.
 */
@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
interface VerlagMapper {

    /**
     * Ein DTO-Objekt von VerlagDTO in ein Objekt für Verlag konvertieren.
     *
     * @param dto DTO-Objekt für VerlagDTO ohne ID.
     * @return Konvertiertes Verlag-Objekt mit null als ID.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "erzeugt", ignore = true)
    @Mapping(target = "aktualisiert", ignore = true)
    @Mapping(target = "fachbereicheStr", ignore = true)
    @Mapping(target = "version", ignore = true)
    Verlag toVerlag(VerlagDTO dto);

    /**
     * Ein DTO-Objekt von VerlagUpdateDTO in ein Objekt für ein zu änderndes Verlag-Objekt konvertieren.
     *
     * @param dto DTO-Objekt für VerlagUpdateDTO ohne ID, version, erzeugt, aktualisiert, hauptsitz, buecher
     * @return Konvertiertes Verlag-Objekt mit null als ID, hauptsitz und buecher
     */
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hauptsitz", ignore = true)
    @Mapping(target = "fachbereicheStr", ignore = true)
    @Mapping(target = "erzeugt", ignore = true)
    @Mapping(target = "buecher", ignore = true)
    @Mapping(target = "aktualisiert", ignore = true)
    Verlag toVerlag(VerlagUpdateDTO dto);

    /**
     * Ein DTO-Objekt von BuchDTO in ein Objekt für Buch konvertieren.
     *
     * @param dto DTO-Objekt für BuchDTO ohne ID.
     * @return Konvertiertes Buch-Objekt mit null als ID.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "erzeugt", ignore = true)
    @Mapping(target = "aktualisiert", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "autorVorname", ignore = true)
    @Mapping(target = "autorNachname", ignore = true)
//    @Mapping(target = "autorVersion", ignore = true)
    Buch toBuch(BuchDTO dto);

    /**
     * Ein DTO-Objekt von AdresseDTO in ein Objekt für Adresse konvertieren.
     *
     * @param dto DTO-Objekt für AdresseDTO.
     * @return Konvertiertes Adresse-Objekt.
     */
    @Mapping(target = "id", ignore = true)
    Adresse toAdresse(AdresseDTO dto);

    /**
     * Ein DTO-Objekt von PreisDTO in ein Objekt für Preis konvertieren.
     *
     * @param dto DTO-Objekt für PreisDTO
     * @return Konvertiertes Preis-Objekt.
     */
    @Mapping(target = "id", ignore = true)
    Preis toPreis(PreisDTO dto);
}
