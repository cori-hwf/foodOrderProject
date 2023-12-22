package com.food.delivery.Helper;

public class BaseContext {
  private static ThreadLocal<Long> threadLocal =
      new ThreadLocal<>(); // we will use it to store userId and hence of type Long

  public static void setCurrentId(Long id) {
    threadLocal.set(id);
  }

  public static Long getCurrentId() {
    return threadLocal.get();
  }
}
