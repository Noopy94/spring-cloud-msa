package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    public GlobalFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchage, chain) -> {
            ServerHttpRequest request = exchage.getRequest();
            ServerHttpResponse response = exchage.getResponse();

            log.info("Custom PRE filter: request id -> {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Global Filter Start: request id -> {}", request.getId() );
            }

            // Custom Post Filter
            return chain.filter(exchage).then(Mono.fromRunnable(()-> {
                if (config.isPreLogger()) {
                    log.info("Global Filter End: request id -> {}", request.getId() );
                }
            }));
        } ;
    }

    @Data
    public static class Config{
        // configuration properties 입력
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
