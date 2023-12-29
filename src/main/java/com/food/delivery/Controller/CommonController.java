package com.food.delivery.Controller;

import com.food.delivery.Helper.Result;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

  @Value("${uploadPath.path}")
  private String basePath;

  @PostMapping("/upload")
  public Result<String> uploadFile(MultipartFile file) {
    // 1. the variable name of MultipartFile must align with the name attribute in
    // Content-Disposition
    // 2. the variable file is stored temporarily, to persistantly store it , we need to do it
    // ourselves
    // log.info(file.toString());

    // check if basePath is an existing directory
    File directory = new File(basePath);
    if (!directory.exists()) directory.mkdirs();

    String originalFilename = file.getOriginalFilename(); // get the file name
    String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

    String fileName =
        UUID.randomUUID().toString() + suffix; // use UUID as fileName to prevent duplicate name
    log.info(fileName);
    try {
      file.transferTo(new File(basePath + fileName)); // save the picture to local
    } catch (IOException e) {
      e.printStackTrace();
    }

    return Result.success(
        fileName); // return the fileName so the fileName can be used to retrieve the picture
  }

  @GetMapping("/download")
  public void downloadFile(String name, HttpServletResponse response) {

    try {
      // get the content using input stream
      FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

      // use output stream to return to the browser
      ServletOutputStream outputStream = response.getOutputStream();

      response.setContentType("image/jpeg");

      byte[] bytes = new byte[1024];
      int eof = 0;

      while ((eof = fileInputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, eof);
        outputStream.flush();
      }

      fileInputStream.close();
      outputStream.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return;
  }
}
