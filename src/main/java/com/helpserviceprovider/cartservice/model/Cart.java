package com.helpserviceprovider.cartservice.model;

import com.helpserviceprovider.cartservice.dto.CartDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

  public Cart(String customerId, CartDto cartDto) {
    this.customerId = customerId;
    this.productIds = new ArrayList<>();
    this.productIds.addAll(cartDto.getProducts());
  }

  private String customerId;

  private List<String> productIds;
}
