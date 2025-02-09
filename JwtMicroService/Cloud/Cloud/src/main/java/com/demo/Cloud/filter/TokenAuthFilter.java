package com.demo.Cloud.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TokenAuthFilter implements GatewayFilter {

    private final WebClient webClient;

    private final String authServerRefreshUrl = "http://localhost:8081/auth/refresh";
    public TokenAuthFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(authHeader==null){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
//        return chain.filter(exchange);
        String token = authHeader.substring("Bearer ".length());

        return webClient.post()
                .uri(authServerRefreshUrl)
                .header("Authorization","Bearer "+token)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                            ObjectMapper objectMapper = new ObjectMapper();
                            try {
                                JsonNode jsonNode = objectMapper.readTree(responseBody);
                                String tokenValue = jsonNode.get("token").asText();

                                ServerWebExchange mutatedExchange = exchange.mutate()
                                        .request(r -> r.header("Authorization", "Bearer " + tokenValue))
                                        .build();
                                return chain.filter(mutatedExchange);
                            } catch (Exception e) {
                                return Mono.error(e);
                            }
        }
                )
                .onErrorResume(e->{
                    e.printStackTrace();
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });

    }

}
