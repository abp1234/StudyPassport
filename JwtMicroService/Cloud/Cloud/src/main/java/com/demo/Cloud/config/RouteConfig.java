package com.demo.Cloud.config;

import com.demo.Cloud.filter.TokenAuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    private final TokenAuthFilter tokenAuthFilter;

    public RouteConfig(TokenAuthFilter tokenAuthFilter){
        this.tokenAuthFilter = tokenAuthFilter;
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("Jwt",r->r.path("/auth/**")
                        .uri("http://localhost:8081"))

                .route("fastapi_hello", r -> r.path("/hello/**")
                        .filters(f -> f.filter(tokenAuthFilter))
                        .uri("http://localhost:8082"))

                .route("fastapi_hi", r -> r.path("/hi/**")
                        .filters(f -> f.filter(tokenAuthFilter))
                        .uri("http://localhost:8083"))

                .route("fastapi_greeting", r -> r.path("/greeting/**")
                        .filters(f -> f.filter(tokenAuthFilter))
                        .uri("http://localhost:8084"))
                .build();

    }
}
