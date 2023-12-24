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

package com.acme.verlag;

import com.acme.verlag.repository.AutorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;

public interface AutorClientConfig {

    int AUTOR_DEFAULT_PORT = 8081;
    Logger LOGGER = LoggerFactory.getLogger(AutorClientConfig.class);

    @Bean
    default UriComponentsBuilder uriComponentsBuilder() {
        // Umgebungsvariable in Kubernetes, sonst: null
        final var autorSchemaEnv = System.getenv("AUTOR_SERVICE_SCHEMA");
        final var autorHostEnv = System.getenv("AUTOR_SERVICE_HOST");
        final var autorPortEnv = System.getenv("AUTOR_SERVICE_PORT");

        final var schema = autorSchemaEnv == null ? "https" : autorSchemaEnv;
        final var host = autorHostEnv == null ? "localhost" : autorHostEnv;
        final int port = autorPortEnv == null ? AUTOR_DEFAULT_PORT : Integer.parseInt(autorPortEnv);

        LOGGER.debug("kunde: host={}, port={}", host, port);
        return UriComponentsBuilder.newInstance()
            .scheme(schema)
            .host(host)
            .port(port);
    }

    @Bean
    default AutorRepository autorRepository(final UriComponentsBuilder uriBuilder,
                                            final RestClient.Builder restClientBuilder,
                                            final RestClientSsl ssl
    ) {
        final var baseUrl = uriComponentsBuilder().build().toUriString();
        LOGGER.info("REST-Client: baseUrl={}", baseUrl);
        final var restClient = restClientBuilder
            .baseUrl(baseUrl)
            .apply(ssl.fromBundle("microservice"))
            .build();
        final var clientAdapter = RestClientAdapter.create(restClient);
        final var proxyFactory = HttpServiceProxyFactory.builderFor(clientAdapter).build();
        return proxyFactory.createClient(AutorRepository.class);
    }
}
