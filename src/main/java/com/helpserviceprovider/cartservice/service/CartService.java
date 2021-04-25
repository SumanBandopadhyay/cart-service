package com.helpserviceprovider.cartservice.service;

import com.helpserviceprovider.cartservice.exception.CartDoesNotExistsException;
import com.helpserviceprovider.cartservice.exception.CustomerIdEmptyException;
import com.helpserviceprovider.cartservice.model.Cart;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CartService {

  private final ReactiveRedisTemplate<String, Cart> cartRedisTemplate;

  public CartService(ReactiveRedisTemplate<String, Cart> cartRedisTemplate) {
    this.cartRedisTemplate = cartRedisTemplate;
  }

  public Flux<Cart> getAllCarts() {
    return cartRedisTemplate.keys("*").flatMap(cartRedisTemplate.opsForValue()::get);
  }

  public Mono<Cart> getCartByCustomerId(String customerId) {
    if (customerId == null) return this.errorCustomerIdNotSent();
    return cartRedisTemplate
        .opsForValue()
        .get(customerId)
        .switchIfEmpty(errorCustomerNotFound(customerId));
  }

  public Mono<Boolean> addToCart(String customerId, Cart cart) {
    if (customerId == null) return this.errorCustomerIdNotSent();
    if (cart.getProductIds().isEmpty()) return Mono.just(false);
    return createNewCart(customerId, cart)
        .flatMap(isCreated -> checkAndProcessIfCreated(isCreated, customerId, cart));
  }

  private Mono<Boolean> createNewCart(String customerId, Cart cart) {
    return cartRedisTemplate.opsForValue().setIfAbsent(customerId, cart);
  }

  private Mono<Boolean> checkAndProcessIfCreated(boolean isCreated, String customerId, Cart cart) {
    if (isCreated) return Mono.just(true);
    return cartRedisTemplate
        .opsForValue()
        .get(customerId)
        .map(inCart -> addProductsToCart(inCart, cart.getProductIds()))
        .flatMap(modifiedCart -> cartRedisTemplate.opsForValue().set(customerId, modifiedCart));
  }

  private Cart addProductsToCart(Cart inCart, List<String> productIds) {
    inCart.getProductIds().addAll(productIds);
    return inCart;
  }

  public Mono<Boolean> removeFromCart(String customerId, Cart cart) {
    if (customerId == null) return this.errorCustomerIdNotSent();
    if (cart.getProductIds().isEmpty()) return Mono.just(false);
    return cartRedisTemplate
        .opsForValue()
        .get(customerId)
        .flatMap(inCart -> removeProductsFromCart(inCart, cart.getProductIds()))
        .flatMap(modifiedCart -> cartRedisTemplate.opsForValue().set(customerId, modifiedCart))
        .switchIfEmpty(errorCustomerNotFound(customerId));
  }

  private Mono<Cart> removeProductsFromCart(Cart inCart, List<String> removedProduct) {
    inCart.getProductIds().removeAll(removedProduct);
    return Mono.just(inCart);
  }

  private <R> Mono<R> errorCustomerNotFound(String customerId) {
    return Mono.error(
        new CartDoesNotExistsException(
            String.format("Cart does not exists for customer id : %s", customerId)));
  }

  private <R> Mono<R> errorCustomerIdNotSent() {
    return Mono.error(new CustomerIdEmptyException("Customer ID is not sent"));
  }
}
