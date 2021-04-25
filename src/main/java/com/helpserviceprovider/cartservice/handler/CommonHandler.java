package com.helpserviceprovider.cartservice.handler;

import com.helpserviceprovider.cartservice.dto.CartDto;
import com.helpserviceprovider.cartservice.dto.error.ApiError;
import com.helpserviceprovider.cartservice.model.Cart;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

public class CommonHandler {

  protected Mono<ServerResponse> badRequestResponse(Throwable e) {
    return badRequest()
        .body(
            Mono.just(
                ApiError.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .timestamp(LocalDateTime.now())
                    .build()),
            ApiError.class);
  }

  protected Mono<ServerResponse> cartResponse(CartDto cart) {
    return ok().body(Mono.just(cart), Cart.class);
  }

  protected Mono<ServerResponse> cartUpdateResponse(Boolean isCartUpdated) {
    return ok().body(Mono.just(isCartUpdated), Cart.class);
  }

  protected Mono<ServerResponse> emptyRequestBody() {
    return badRequest()
        .body(
            Mono.just(
                ApiError.builder()
                    .message("Payload cannot be empty")
                    .status(HttpStatus.BAD_REQUEST)
                    .timestamp(LocalDateTime.now())
                    .build()),
            ApiError.class);
  }
}
