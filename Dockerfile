# syntax=docker/dockerfile:1.6.0

# Copyright (C) 2020 - present Juergen Zimmermann, Hochschule Karlsruhe
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

# Aufruf:   docker buildx build --sbom true --tag juergenzimmermann/kunde:2023.10.0-eclipse .
#               ggf. --progress=plain
#               ggf. --no-cache
#           Get-Content Dockerfile | docker run --rm --interactive hadolint/hadolint:2.12.1-beta-debian
#           docker compose --profile kunde up
#           docker compose exec kunde bash

# https://docs.docker.com/engine/reference/builder/#syntax
# https://github.com/moby/buildkit/blob/master/frontend/dockerfile/docs/syntax.md
# https://hub.docker.com/r/docker/dockerfile
# https://docs.docker.com/build/building/multi-stage
# https://containers.gitbook.io/build-containers-the-hard-way


ARG JAVA_VERSION=21.0.0

# ---------------------------------------------------------------------------------------
# S t a g e :   b u i l d e r
#
#   "Eclipse Temurin" aus "Eclipse Adoptium Project" als JDK https://github.com/adoptium/containers
#   Ubuntu "Jammy Jellyfish" 22.04 LTS (Long Term Support, April 2027 / 2032) https://ubuntu.com/about/release-cycle https://wiki.ubuntu.com/Releases
#   Docker-Image fuer OpenJDK ist *deprecated*
#   Amazon Coretto: nur JDK, nicht JRE (s.u.)
#   SapMachine: nur JDK, nicht JRE (s.u.)
#   JAR bauen mit eigenem Code und Dependencies z.B. Spring, Jackson
# ---------------------------------------------------------------------------------------
FROM eclipse-temurin:${JAVA_VERSION}-jdk-jammy AS builder
#FROM bellsoft/liberica-openjdk-debian:${JAVA_VERSION} AS builder

# "working directory" fuer die Docker-Kommandos RUN, ENTRYPOINT, CMD, COPY und ADD
WORKDIR /source

# ADD hat mehr Funktionalitaet als COPY, z.B. auch Download von externen Dateien
COPY build.gradle.kts gradle.properties gradlew settings.gradle.kts ./
COPY gradle ./gradle
COPY src ./src

# JAR-Datei mit den Schichten ("layers") erstellen und aufbereiten bzw. entpacken
RUN <<EOF
./gradlew --no-configuration-cache --no-daemon --no-watch-fs bootJar
java -Djarmode=layertools -jar ./build/libs/kunde-2023.10.0.jar extract
EOF

# ---------------------------------------------------------------------------------------
# S t a g e   F i n a l
#
#   JRE statt JDK
#   Dependencies: z.B. Spring, Jackson
#   Loader fuer Spring Boot
#   Eigener uebersetzter Code
# ---------------------------------------------------------------------------------------

FROM eclipse-temurin:${JAVA_VERSION}-jre-jammy
#FROM bellsoft/liberica-openjre-debian:${JAVA_VERSION}

# Anzeige bei "docker inspect ..."
# https://specs.opencontainers.org/image-spec/annotations
# https://spdx.org/licenses
# https://snyk.io/de/blog/how-and-when-to-use-docker-labels-oci-container-annotations
# MAINTAINER ist deprecated https://docs.docker.com/engine/reference/builder/#maintainer-deprecated
LABEL org.opencontainers.image.title="kunde" \
      org.opencontainers.image.description="Microservice kunde v0 mit Basis-Image Eclipse Temurin und Ubuntu Jammy" \
      org.opencontainers.image.version="2023.10.0-eclipse" \
      org.opencontainers.image.licenses="GPL-3.0-or-later" \
      org.opencontainers.image.vendor="Juergen Zimmermann" \
      org.opencontainers.image.authors="Juergen.Zimmermann@h-ka.de" \
      org.opencontainers.image.base.name="eclipse-temurin:LATEST_VERSION-jre-jammy"

WORKDIR /opt/app

# "here document" wie in einem Shellscipt
# https://unix.stackexchange.com/questions/217369/clear-apt-get-list
RUN <<EOF
set -ex
apt-get update
apt-get upgrade --yes
apt-get autoremove -y
apt-get clean -y
rm -rf /var/lib/apt/lists/*
groupadd --gid 1000 app
useradd --uid 1000 --gid app --no-create-home app
chown -R app:app /opt/app
EOF

# ADD hat mehr Funktionalitaet als COPY, z.B. auch Download von externen Dateien
# ggf. auch /source/snapshot-dependencies/
COPY --from=builder --chown=app:app /source/dependencies/ /source/spring-boot-loader/ /source/application/ ./

USER app
EXPOSE 8080

# Bei CMD statt ENTRYPOINT kann das Kommando bei "docker run ..." ueberschrieben werden
ENTRYPOINT ["java", "--enable-preview", "org.springframework.boot.loader.JarLauncher"]
