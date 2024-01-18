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

package com.acme.verlag.repository;

import com.acme.verlag.entity.Verlag;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.acme.verlag.entity.Verlag.*;

/**
 * Repository für den DB-Zugriff bei Verlagen.
 */
@Repository
public interface VerlagRepository extends JpaRepository<Verlag, UUID>, JpaSpecificationExecutor<Verlag> {

    @EntityGraph(HAUPTSITZ_BUECHER_GRAPH)
    @NonNull
    @Override
    List<Verlag> findAll();

    @EntityGraph(HAUPTSITZ_BUECHER_GRAPH)
    @NonNull
    @Override
    List<Verlag> findAll(@NonNull Specification<Verlag> spec);

    @EntityGraph(HAUPTSITZ_BUECHER_GRAPH)
    @NonNull
    @Override
    Optional<Verlag> findById(@NonNull UUID id);
}
