package com.food.delivery.Exception;

import com.food.delivery.Helper.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = {RestController.class, Controller.class}) // 作用域
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(DataIntegrityViolationException.class)
  public Result<String> exceptionHandler(DataIntegrityViolationException e) {
    if (e.getMessage().contains("duplicate key value violates unique constraint")) {
      String keyword = e.getMessage().split("Detail: Key \\(")[1].split("\\)")[0];
      log.info(keyword);
      return Result.error(keyword + " has already exist");
    }
    return Result.error("Unkown DataIntegrityViolation error");
  }

  @ExceptionHandler(CustomerizedException.class)
  public Result<String> exceptionHandler(CustomerizedException e) {
    return Result.error(e.getMessage());
  }
}
