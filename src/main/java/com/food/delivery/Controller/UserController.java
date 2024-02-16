package com.food.delivery.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.food.delivery.Entity.User;
import com.food.delivery.Helper.OtpGenerator;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.EmailService;
import com.food.delivery.Service.UserService;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired private EmailService emailService;

  @Autowired private UserService userService;

  @Autowired private RedisTemplate redisTemplate;

  @PostMapping("/sendMsg")
  public Result<String> senMsg(@RequestBody User user, HttpSession session) {

    String email = user.getEmail();

    if (!(email.isEmpty())) {
      // generate otp
      Integer Otp = OtpGenerator.generateValidateCode(6);

      log.info("the otp is {}", Otp);
      // send otp
      emailService.sendSimpleMail(email, "Your Otp to login", "Your Otp to login is " + Otp);

      // save the otp to session
      // session.setAttribute(email, Otp);

      // save the otp to redis for timeLimit of 5 minutes
      redisTemplate.opsForValue().set(email, Otp, 5, TimeUnit.MINUTES);

      return Result.success("Message sent successfully");
    }

    return Result.error("Message sent unsuccessfully");
  }

  @PostMapping("login")
  public Result<String> login(@RequestBody Map map, HttpSession session) {

    String email = map.get("email").toString();

    String otp = map.get("code").toString();

    // validate if otp in session with the email and is correct
    // String otpInSession = session.getAttribute(email).toString();
    String otpInRedis = redisTemplate.opsForValue().get(email).toString();

    if (otpInRedis != null && otpInRedis.equals(otp)) {
      // create the user record in db if user doesn't exist
      LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
      lambdaQueryWrapper.eq(User::getEmail, email);
      User user = userService.getOne(lambdaQueryWrapper);

      // save the user if null
      if (user == null) {
        user = new User();
        user.setEmail(email);
        // user.getStatus(1) autoSet at db level
        userService.save(user);
      }
      session.setAttribute("user", user.getId());

      // user successfully logged in hence the otp shall not be valid anymore
      redisTemplate.delete(email);

      return Result.success("User log in successfully");
    }

    return Result.error("User log in unsuccessfully");
  }

  @PostMapping("/loginout")
  public Result<String> logout(HttpSession session) {
    session.removeAttribute("user");

    return Result.success("Log out successfully");
  }
}
