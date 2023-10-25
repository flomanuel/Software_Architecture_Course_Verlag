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

package com.acme.verlag.rest;

import com.acme.verlag.service.VerlagReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller-Klasse, bildet die REST-Schnittstelle ab.
 */
@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class VerlagController {

    /**
     * Muster für eine UUID.
     */
    public static final String ID_PATTERN =
        "[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}";

    /**
     * Service für den Verlag.
     */
    private final VerlagReadService service;


    /**
     * Suche anhand der Verlags-ID als Pfad-Parameter.
     *
     * @param id ID des zu suchenden Verlags.
     * @return Gefundener Verlag.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = APPLICATION_JSON_VALUE)
    VerlagModel getById(@PathVariable final UUID id) {
        return new VerlagModel(service.findById(id));
    }

    /**
     * Abfrage aller Verlage.
     *
     * @return Alle gespeicherten Verlage als Liste.
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    List<VerlagModel> get() {
        return service.findAll().stream().map(VerlagModel::new).toList();
    }
}
