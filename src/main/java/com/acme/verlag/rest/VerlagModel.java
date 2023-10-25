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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@JsonPropertyOrder({
    "id","name", "gruendungsjahr", "hauptsitz", "buecher"
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString(callSuper = true)
public class VerlagModel extends RepresentationModel<VerlagModel> {
    @EqualsAndHashCode.Include
    private final UUID id;
    private final String name;
    private final Year gruendungsjahr;
    private final Adresse hauptsitz;
    private final List<Buch> buecher;


    VerlagModel(final Verlag verlag) {
        this.id = verlag.getId();
        this.name = verlag.getName();
        this.gruendungsjahr = verlag.getGruendungsjahr();
        this.hauptsitz = verlag.getHauptsitz();
        this.buecher = verlag.getBuecher();
    }
}
