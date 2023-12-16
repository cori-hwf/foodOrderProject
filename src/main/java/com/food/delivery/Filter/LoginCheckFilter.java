package com.food.delivery.Filter;

import com.alibaba.fastjson.JSON;
import com.food.delivery.Helper.Result;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

  // path matcher provided by spring that supports wildcard like **
  public static final AntPathMatcher pathMatcher = new AntPathMatcher();

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String requestURI = httpRequest.getRequestURI();

    // log.info("executed {}",requestURI);

    String[] URLs =
        new String[] {
          "/employee/login", "/employee/logout", "/backend/**", "/frontend/**"
        }; // list of urls that need not to be checked

    // if matched any of the url need not to check
    if (checkUrl(URLs, requestURI)) {
      chain.doFilter(httpRequest, httpResponse);
      return;
    }

    // else we need to check if the user has already logged in
    if (httpRequest.getSession().getAttribute("currEmployee") != null) {
      log.info(
          "it is currently logged in with employeeId {}",
          httpRequest.getSession().getAttribute("currEmployee"));
      chain.doFilter(httpRequest, httpResponse);
      return;
    }

    // not logged in we need to return data to client for the frontend interceptor to redirect the
    // website
    httpResponse.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
    // log.info("it reaches the NOTLOGIN {}",requestURI);
    return;
  }

  public boolean checkUrl(String[] urls, String requestUrl) {
    for (String currUrl : urls) {
      boolean matched = pathMatcher.match(currUrl, requestUrl);
      if (matched) return true;
    }
    return false;
  }
}
