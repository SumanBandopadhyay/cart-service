package com.helpserviceprovider.cartservice.handler;

import com.helpserviceprovider.cartservice.dto.CartDto;
import com.helpserviceprovider.cartservice.model.Cart;
import com.helpserviceprovider.cartservice.service.CartService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class CartHandler extends CommonHandler {

  private static final String CUSTOMER_ID = "customerId";

  private final CartService cartService;

  public CartHandler(CartService cartService) {
    this.cartService = cartService;
  }

  public Mono<ServerResponse> getCartById(ServerRequest serverRequest) {
    String customerId = serverRequest.headers().firstHeader(CUSTOMER_ID);
    return cartService
        .getCartByCustomerId(customerId)
        .map(CartDto::new)
        .flatMap(this::cartResponse)
        .onErrorResume(this::badRequestResponse);
  }

  public Mono<ServerResponse> addToCart(ServerRequest serverRequest) {
    Mono<CartDto> cartRequest = serverRequest.bodyToMono(CartDto.class);
    String customerId = serverRequest.headers().firstHeader(CUSTOMER_ID);
    return cartRequest
        .flatMap(cart -> cartService.addToCart(customerId, new Cart(customerId, cart)))
        .flatMap(this::cartUpdateResponse)
        .onErrorResume(this::badRequestResponse)
        .switchIfEmpty(emptyRequestBody());
  }

  public Mono<ServerResponse> getAllCarts() {
    var carts = cartService.getAllCarts();
    var cartDtos = carts.map(CartDto::new);
    return ok().body(cartDtos, Cart.class);
  }

  public Mono<ServerResponse> removeFromCart(ServerRequest serverRequest) {
    Mono<CartDto> cartRequest = serverRequest.bodyToMono(CartDto.class);
    String customerId = serverRequest.headers().firstHeader(CUSTOMER_ID);
    return cartRequest
        .flatMap(cart -> cartService.removeFromCart(customerId, new Cart(customerId, cart)))
        .flatMap(this::cartUpdateResponse)
        .onErrorResume(this::badRequestResponse)
        .switchIfEmpty(emptyRequestBody());
  }
}
