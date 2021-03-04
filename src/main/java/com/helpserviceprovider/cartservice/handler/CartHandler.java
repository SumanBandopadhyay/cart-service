package com.helpserviceprovider.cartservice.handler;

import com.helpserviceprovider.cartservice.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.created;

@Component
public class CartHandler {

  @Autowired private ReactiveRedisOperations<String, Cart> cartOperations;

  public Mono<ServerResponse> getCartById(ServerRequest serverRequest) {
    String id = serverRequest.pathVariable("id");
    Mono<Cart> cart = cartOperations.opsForValue().get(id);
    return ok().body(cart, Cart.class);
  }

  public Mono<ServerResponse> createCart(ServerRequest serverRequest) {
    Mono<Cart> cartRequest = serverRequest.bodyToMono(Cart.class);
    return cartRequest.flatMap(
        cart -> {
          Mono<Boolean> isCartCreated =
              cartOperations.opsForValue().setIfAbsent(cart.getCustomerId(), cart);
          return created(URI.create(String.format("/api/cart/%s", cart.getCustomerId())))
              .body(isCartCreated, Boolean.class);
        });
  }

  public Mono<ServerResponse> updateCart(ServerRequest serverRequest) {
    Mono<Cart> cartRequest = serverRequest.bodyToMono(Cart.class);
    Mono<Boolean> isCartUpdated =
        cartRequest.flatMap(
            cart -> cartOperations.opsForValue().setIfPresent(cart.getCustomerId(), cart));
    return ok().body(isCartUpdated, Boolean.class);
  }

  public Mono<ServerResponse> getAllCarts(ServerRequest serverRequest) {
    Flux<Cart> carts = cartOperations.keys("*").flatMap(cartOperations.opsForValue()::get);
    return ok().body(carts, Cart.class);
  }
}
