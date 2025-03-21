package exam.jsb_webshop_t2406e.security.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import exam.jsb_webshop_t2406e.security.services.UserDetailsServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
// by Phuong
// lodaKun là: JwtAuthenticationFilter.java
public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  /**
   * Vấn đề:
   * https://stackoverflow.com/questions/50673400/how-to-log-in-by-email-instead-of-username-in-spring-security
   * 
   * Giải pháp là: Customer đăng ký tài khoản bằng Email, hệ thống sẽ tự động tạo Username từ Email.
   * Khi Customer đăng nhập bằng Email, chúng ta sẽ dùng thẻ <input type="email" name="username"> trong form login.
   * Tức là đây là một thủ thuật: label thì là email, nhưng <input name="username"> vẫn là username.
   * 
   * Như vậy là ta có thể tái sử dụng được hệ thống User Role đã có sẵn, không cần phải sửa đổi gì nhiều.
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(userDetails,
                                                    null,
                                                    userDetails.getAuthorities());
        
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }

    // bổ sung mã vào đây:
    filterChain.doFilter(request, response);
  }

  // so sánh JWT Cookies và JWT localStorage
  // https://stackoverflow.com/questions/34817617/should-jwt-be-stored-in-localstorage-or-cookie
  private String parseJwt(HttpServletRequest request) {
    String jwt = jwtUtils.getJwtFromCookies(request);
    return jwt;
  }
}
