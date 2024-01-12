package com.food.delivery.Helper;

import java.util.Random;

public class OtpGenerator {

  public static Integer generateValidateCode(int length) {
    Integer code = null;
    if (length == 4) {
      code = new Random().nextInt(9999); // generate random number max 9999
      if (code < 1000) {
        code = code + 1000; // ensure 4 digits
      }
    } else if (length == 6) {
      code = new Random().nextInt(999999);
      if (code < 100000) {
        code = code + 100000;
      }
    } else {
      throw new RuntimeException("Support only 4 digit or 6 digit Otp generation");
    }
    return code;
  }
}
