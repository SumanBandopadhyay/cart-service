package com.helpserviceprovider.cartservice.resource;

import com.helpserviceprovider.cartservice.handler.CartHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class CartResource {

  private final CartHandler cartHandler;

  public CartResource(CartHandler cartHandler) {
    this.cartHandler = cartHandler;
  }

  @Bean
  RouterFunction<ServerResponse> routes() {
    return route(GET("/api/cart"), cartHandler::getCartById)
        .andRoute(GET("/api/cart/all"), request -> cartHandler.getAllCarts())
        .andRoute(POST("/api/cart/add"), cartHandler::addToCart)
        .andRoute(DELETE("/api/cart/remove"), cartHandler::removeFromCart);
  }
}
