package com.food.delivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
// @MapperScan("com.food.delivery.Mapper") //so that need not to use Mapper annotation in all the
// mapper interface
public class DeliveryApplication {

  public static void main(String[] args) {
    SpringApplication.run(DeliveryApplication.class, args);
    log.info("project starting...");
  }
}
