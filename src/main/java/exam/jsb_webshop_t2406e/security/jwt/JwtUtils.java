package exam.jsb_webshop_t2406e.security.jwt;
import java.security.Key;
import java.util.Date;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import exam.jsb_webshop_t2406e.security.services.UserDetailsImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
// import io.jsonwebtoken.security.SecretK


/* // https://stackoverflow.com/questions/78805779/issue-with-parserbuilder-method-in-jjwt-library-for-jwt-token-validation
 * parseBuilder was changed to parser() in 0.12 Version.

If you want it to work just change it to parser():

Jwts.parser()
    .setSigningKey(key)
    .build()
    .parseClaimsJws(jwt)
    .getBody();

    How to access a value defined in the application.properties file in Spring Boot

https://stackoverflow.com/questions/30528255/how-to-access-a-value-defined-in-the-application-properties-file-in-spring-boot

 */

 // giống với JwtTokenProvider.java của lodaKun
 // cung cấp các tiện ích cơ bản như là: lấy JWT từ Cookie máy khách
 // Sinh ra chuỗi JWT
@Component
public class JwtUtils 
{
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${fptaptech.app.jwtSecret}") // Đọc dữ liệu từ application.properties // 
  private String jwtSecret;

  @Value("${fptaptech.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${fptaptech.app.jwtCookieName}")
  private String jwtCookie;

  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }

  // Tạo chuỗi JWT từ danh tính, tài khoản của người dùng (chủ yếu là từ Username)
  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
    String jwt = generateTokenFromUsername(userPrincipal.getUsername());
    ResponseCookie cookie = ResponseCookie
                            .from(jwtCookie, jwt)
                            .path("/api")
                            .maxAge(24 * 60 * 60)
                            .httpOnly(true).build();
    return cookie;
  }

  public ResponseCookie getCleanJwtCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
    return cookie;
  }

  // Đọc danh tính người dùng từ chuỗi JWT máy khách gửi
  public String getUserNameFromJwtToken(String token) {
    return Jwts
            .parserBuilder() // 0.11.5
            // .parser() // 0.12.6
            .setSigningKey(key())
            // .verifyWith(key()) // 0.12.6
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }
  
  // Tạo khóa bí mật
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  // Xác thực chuỗi JWT gửi từ máy khách xem có khớp với tài khoản trên hệ thống hay không ???
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts
      .parserBuilder() // 0.11.5
      // .parser() // 0.12.6
      .setSigningKey(key())
      .build()
      .parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
  
  public String generateTokenFromUsername(String username) {   
    return Jwts.builder()
              .setSubject(username)
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
              .signWith(key(), SignatureAlgorithm.HS256)
              .compact();
  }
}
