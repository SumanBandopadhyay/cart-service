package com.helpserviceprovider.cartservice.dto;

import com.helpserviceprovider.cartservice.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

  public CartDto(Cart cart) {
    products = cart.getProductIds();
  }

  private List<String> products;
}
