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

package com.acme.verlag.graphql;

import com.acme.verlag.entity.Adresse;
import com.acme.verlag.entity.Buch;
import com.acme.verlag.entity.Preis;
import com.acme.verlag.entity.Verlag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

/**
 * Mapper zwischen Entity-Klassen. Siehe build\generated\sources\annotationProcessor\java\...
 * \VerlagInputMapperImpl.java.
 */
@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
interface VerlagInputMapper {

    /**
     * Ein VerlagInput-Objekt in ein Objekt für Verlage konvertieren.
     *
     * @param input VerlagInput ohne ID.
     * @return Konvertiertes Verlag-Objekt.
     */
    @Mapping(target = "id", ignore = true)
    Verlag toVerlag(VerlagInput input);

    /**
     * Ein BuchInput-Objekt in ein Objekt für Bücher konvertieren.
     *
     * @param input BuchInput ohne ID.
     * @return Konvertiertes Buch-Objekt.
     */
    @Mapping(target = "id", ignore = true)
    Buch toBuch(BuchInput input);

    /**
     * Ein AdresseInput-Objekt in ein Objekt für Adressen konvertieren.
     *
     * @param input AdresseInput ohne ID.
     * @return Konvertiertes Adresse-Objekt.
     */
    Adresse toAdresse(AdresseInput input);

    /**
     * Ein PreisInput-Objekt in ein Objekt für Preise konvertieren.
     *
     * @param input PreisInput ohne ID.
     * @return Konvertiertes Preis-Objekt.
     */
    Preis toPreis(PreisInput input);
}
