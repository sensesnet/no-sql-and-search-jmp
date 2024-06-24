package com.epam.jmp;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.epam.jmp.configs.ElasticsearchConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger("MAIN");

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        logger.info("APPLICATION started.");
    }

    @Bean
    public RestClient restClient(ElasticsearchConfig config) {
        return RestClient.builder(new HttpHost(config.getElasticsearchHost(), config.getElasticsearchPort())).build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}