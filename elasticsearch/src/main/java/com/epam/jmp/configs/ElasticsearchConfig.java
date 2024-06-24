package com.epam.jmp.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${elasticsearch.port}")
    private int elasticsearchPort;

    @Value("${elasticsearch.low}")
    private boolean useLowLevel;

    public String getElasticsearchHost() {
        return elasticsearchHost;
    }

    public void setElasticsearchHost(String elasticsearchHost) {
        this.elasticsearchHost = elasticsearchHost;
    }

    public int getElasticsearchPort() {
        return elasticsearchPort;
    }

    public void setElasticsearchPort(int elasticsearchPort) {
        this.elasticsearchPort = elasticsearchPort;
    }

    public boolean useLowLevel() {
        return useLowLevel;
    }

    public void setUseLowLevel(boolean useLowLevel) {
        this.useLowLevel = useLowLevel;
    }
}
