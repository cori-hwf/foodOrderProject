package com.food.delivery.Exception;

public class DishNotFoundException extends RuntimeException {
  public DishNotFoundException(String message) {
    super(message);
  }
}
