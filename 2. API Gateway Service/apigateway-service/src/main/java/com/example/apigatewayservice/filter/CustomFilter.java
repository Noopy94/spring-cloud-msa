package com.example.apigatewayservice.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        //Custom pre filter
        return (exchage, chain) -> {
            ServerHttpRequest request = exchage.getRequest();
            ServerHttpResponse response = exchage.getResponse();
            log.info("Custom PRE filter: request id -> {}", request.getId());

            // Custom Post Filter
            return chain.filter(exchage).then(Mono.fromRunnable(()-> {
                log.info("Custom Post filter: response code -> {}", response.getStatusCode());
            }));
        } ;
    }

    public static class Config {
        // put the configuration properties
    }


}
