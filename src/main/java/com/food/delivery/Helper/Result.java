package com.food.delivery.Helper;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

// a generic return type
// All response to the clients' requests will be encapsulated into this class

@Data
public class Result<T> {

  private Integer code;

  private String msg;

  private T data;

  private Map map = new HashMap();

  // the first <T> declares that this is a generic method
  // Result<T> is the return type
  public static <T> Result<T> success(T object) {
    Result<T> res = new Result<T>();
    res.code = 1;
    res.data = object;
    return res;
  }

  public static <T> Result<T> error(String errorMessage) {
    Result<T> res = new Result<T>();
    res.code = 0; // signifies error
    res.msg = errorMessage;
    return res;
  }

  public Result<T> add(String key, Object val) {
    this.map.put(key, val);
    return this;
  }
}
