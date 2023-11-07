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

import com.acme.verlag.entity.Verlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * todo: wtf sollen wir mit dem decoratod aus den VL-Folien machen?
 * Also ich kapiers jetzt glaube ich. Ist halt ein Decorator. Falls ich was im generierten VerlagMapper umrum hinzufügen möchte.
 * Bei uns ja aber eigentlich egal. Also ich habe bisher keine extra technische Funktionalität. Daher kann der eigentlich weg.
 * Ich kann halt im "VerlagWriteController" anstelle des Interfaces "VerlagMapper" diese Klasse hier angeben. Scheint auch zu funktionieren.
 * Weil ich halt als Delegat die generierte Klasse erhalte, welche das Interface "VerlagMapper" realisiert.
 */
abstract class AbstractVerlagMapperDecorator implements VerlagMapper {

    @Autowired
    @Qualifier("delegate")
    private VerlagMapper delegate;

    @Override
    public Verlag toVerlag(final VerlagDTO dto) {
        return delegate.toVerlag(dto);
    }
}
