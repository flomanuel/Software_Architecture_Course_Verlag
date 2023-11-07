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

package com.acme.verlag.service;

import com.acme.verlag.entity.Verlag;
import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Collection;

/**
 * Exception, falls es mindestens ein verletztes Constrains gibt.
 */
@Getter
public class ConstraintViolationsException extends RuntimeException {

    /**
     * Die verletzten Constraints.
     */
    private final transient Collection<ConstraintViolation<Verlag>> violations;

    ConstraintViolationsException(
        final Collection<ConstraintViolation<Verlag>> violations
    ) {
        super("Constraints sind verletzt.");
        this.violations = violations;
    }
}
