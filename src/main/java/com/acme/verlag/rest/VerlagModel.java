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
import com.acme.verlag.entity.Verlag;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.List;

/**
 * Model-Klasse für Spring HATEOAS.
 */
@Relation(collectionRelation = "verlage", itemRelation = "verlag")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = true)
public class VerlagModel extends RepresentationModel<VerlagModel> {

    private final String name;
    private final LocalDate gruendungsdatum;
    private final Adresse hauptsitz;
    private List<Buch> buecher;

    VerlagModel(final Verlag verlag) {
        name = verlag.getName();
        gruendungsdatum = verlag.getGruendungsdatum();
        hauptsitz = verlag.getHauptsitz();
        buecher = verlag.getBuecher();
    }
}
