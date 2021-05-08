package com.helpserviceprovider.cartservice.service;

import com.helpserviceprovider.cartservice.exception.CartDoesNotExistsException;
import com.helpserviceprovider.cartservice.exception.CustomerIdEmptyException;
import com.helpserviceprovider.cartservice.model.Cart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

  @Mock private ReactiveRedisTemplate<String, Cart> cartRedisTemplate;

  @InjectMocks
  private CartService cartService;

  @Test
  void when_get_cart_exists_for_customer() {
    ReactiveValueOperations<String, Cart> cartValueOperations = mock(ReactiveValueOperations.class);
    when(cartRedisTemplate.opsForValue()).thenReturn(cartValueOperations);
    Cart cart = Cart.builder().customerId("customerId").build();
    when(cartValueOperations.get(anyString()))
            .thenReturn(Mono.just(cart));
    Mono<Cart> result = cartService.getCartByCustomerId("customerId");
    StepVerifier.create(result).expectNext(cart).verifyComplete();
  }

  @Test
  void when_get_cart_does_not_exists_for_customer() {
    ReactiveValueOperations<String, Cart> cartValueOperations = mock(ReactiveValueOperations.class);
    when(cartRedisTemplate.opsForValue()).thenReturn(cartValueOperations);
    when(cartValueOperations.get(anyString()))
            .thenReturn(Mono.empty());
    Mono<Cart> result = cartService.getCartByCustomerId("");
    StepVerifier.create(result).expectError(CartDoesNotExistsException.class).verify();
  }

  @Test
  void when_get_cart_does_not_provide_customer_id() {
    Mono<Cart> result = cartService.getCartByCustomerId(null);
    StepVerifier.create(result).expectError(CustomerIdEmptyException.class).verify();
  }
}
