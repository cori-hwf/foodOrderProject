package com.food.delivery.Service.ServiceImp;

import com.food.delivery.Exception.CustomerizedException;
import com.food.delivery.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImp implements EmailService {

  @Autowired private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String from;

  @Override
  public void sendSimpleMail(String to, String subject, String content) {

    try {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

      simpleMailMessage.setFrom(from);
      simpleMailMessage.setTo(to);
      simpleMailMessage.setSubject(subject);
      simpleMailMessage.setText(content);

      // send the email
      mailSender.send(simpleMailMessage);

    } catch (Exception e) {
      throw new CustomerizedException("email sent error");
    }
  }
}
