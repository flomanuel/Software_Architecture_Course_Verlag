///*
// * Copyright (c) 2023 - present Florian Sauer
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <http://www.gnu.org/licenses/>.
// */
//
//package com.acme.verlag.graphql;
//
//import com.acme.verlag.entity.Buch;
//import com.acme.verlag.entity.Verlag;
//import org.mapstruct.Mapper;
//import org.mapstruct.NullValueMappingStrategy;
//import org.mapstruct.Qualifier;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.Year;
//
//abstract class AbstractVerlagMapperDecorator implements VerlagInputMapper {
//
//    @Autowired
//    @Qualifier("delegate")
//    private VerlagInputMapper delegate;
//
//    @Override
//    public Verlag toVerlag(final VerlagInput input) {
//        final var verlag = delegate.toVerlag(input);
//        final var gruendungsjahr = Year.parse(input.gruendungsjahr());
//        verlag.setGruendungsjahr(gruendungsjahr);
//        return verlag;
//    }
//
//    @Override
//    public Buch toBuch(final BuchInput input) {
//        final var buch = delegate.toBuch(input);
//        final var erscheinungsjahr = Year.parse(input.erscheinungsjahr());
//        buch.setErscheinungsjahr(erscheinungsjahr);
//        return buch;
//    }
//}
