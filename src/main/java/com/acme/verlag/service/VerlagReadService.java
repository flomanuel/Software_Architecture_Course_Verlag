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

package com.acme.verlag.service;

import com.acme.verlag.entity.Verlag;
import com.acme.verlag.repository.Autor;
import com.acme.verlag.repository.AutorRepository;
import com.acme.verlag.repository.SpecificationBuilder;
import com.acme.verlag.repository.VerlagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Anwendungslogik für Verlage.
 * <p/>
 * <img src="../../../../../asciidoc/VerlagReadService.svg" alt="Klassendiagramm">
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VerlagReadService {

    private final VerlagRepository verlagRepository;
    private final AutorRepository autorRepository;
    private final SpecificationBuilder specificationBuilder;

    /**
     * Einen Verlag anhand seiner ID suchen.
     *
     * @param id Die ID des gesuchten Verlags.
     * @return Der gefundene Verlag.
     * @throws NotFoundException Falls kein Verlag gefunden wurde.
     */
    public Verlag findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var verlag = verlagRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        log.debug("findById: {}", verlag);
        this.addAuthorInfo(verlag);
        return verlag;
    }

    /**
     * Verlage anhand von Suchkriterien als Collection suchen.
     * Gibt es keine Suchkriterien, werden alle Verlage gesucht.
     *
     * @param suchkriterien Die Suchkriterien
     * @return Die gefundenen Verlage oder eine leere Liste.
     */
    @SuppressWarnings({"ReturnCount", "NestedIfDepth"})
    public Collection<Verlag> find(final Map<String, List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);
        if (suchkriterien.isEmpty()) {
            final var verlage = verlagRepository.findAll();
            verlage.forEach(this::addAuthorInfo);
            log.debug("find: {}", verlage);
            return verlage;
        }
        final var spec = specificationBuilder
            .build(suchkriterien)
            .orElseThrow(() -> new NotFoundException(suchkriterien));
        final var verlage = verlagRepository.findAll(spec);
        if (verlage.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        verlage.forEach(this::addAuthorInfo);
        log.debug("find: {}", verlage);
        return verlage;
    }

    private void addAuthorInfo(Verlag verlag) {
        verlag.getBuecher().forEach(
            buch -> {
                final var autor = findAutorById(buch.getAutorId());
                var vorname = autor.vorname();
                buch.setAutorVorname(vorname);
                var nachname = autor.nachname();
                buch.setAutorNachname(nachname);
            });
    }

    /**
     * Autor zur Autor-ID suchen.
     *
     * @param autorId Die ID des gegebenen Autors.
     * @return Der gefundene Autor.
     */
    public Autor findAutorById(final UUID autorId) {
        log.debug("findAutorById: autorId={}", autorId);
        final ResponseEntity<Autor> autorResponse;
        try {
            autorResponse = autorRepository.getById(autorId);
        } catch (final HttpClientErrorException.NotFound ex) {
            // 404
            log.debug("findAutorById: HttpClientErrorException.NotFound");
            return new Autor("N/A", "N/A");
        } catch (final HttpStatusCodeException ex) {
            // 4xx oder 5xx
            log.debug("findAutorById", ex);
            return new Autor("Exception", "Exception");
        } catch (final ResourceAccessException ex) {
            // Falls der zweite Server gar nicht gestartet ist.
            log.debug("findAutorById", ex);
            return new Autor("Exception", "Exception");
        }
        final var autor = autorResponse.getBody();
        log.debug("findAutorById: {}", autor);
        return autor;
    }
}
