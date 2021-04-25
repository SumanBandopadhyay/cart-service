package com.helpserviceprovider.cartservice.service;

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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

  @Mock private ReactiveRedisTemplate<String, Cart> cartRedisTemplate;

  @Mock private ReactiveValueOperations<String, Cart> cartValueOperations;

  @InjectMocks
  private CartService cartService;

  @Test
  void when_get_cart_exists_for_customer() {
    when(cartRedisTemplate.hasKey(anyString())).thenReturn(Mono.just(true));
    when(cartRedisTemplate.opsForValue()).thenReturn(cartValueOperations);
    when(cartValueOperations.get(anyString()))
            .thenReturn(Mono.just(new Cart()));
//            .thenReturn(any(Mono.class));
    StepVerifier.create(cartService.getCartByCustomerId(anyString())).verifyComplete();
  }

//  @Test
//  void when_create_cart() {
//
//  }
}
