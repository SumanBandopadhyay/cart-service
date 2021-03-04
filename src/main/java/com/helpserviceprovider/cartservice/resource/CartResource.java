package com.helpserviceprovider.cartservice.resource;

import com.helpserviceprovider.cartservice.handler.CartHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class CartResource {

    @Autowired private CartHandler cartHandler;

    @Bean
    RouterFunction<ServerResponse> routes() {
        return route(GET("/api/cart/{id}"), cartHandler::getCartById)
                .andRoute(GET("/api/cart/all"), cartHandler::getAllCarts)
                .andRoute(POST("/api/cart/create"), cartHandler::createCart)
                .andRoute(PUT("/api/cart/{id}"), cartHandler::updateCart);
    }

}
