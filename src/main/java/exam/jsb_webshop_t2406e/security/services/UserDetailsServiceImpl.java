package exam.jsb_webshop_t2406e.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import exam.jsb_webshop_t2406e.models.user.User;
import exam.jsb_webshop_t2406e.models.user.UserRepository;

// by Phuong & bezKoder
// bên LodaKun là: UserService
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  /**
   * Vấn đề:
   * https://stackoverflow.com/questions/50673400/how-to-log-in-by-email-instead-of-username-in-spring-security
   * 
   * Giải pháp là: Customer đăng ký tài khoản bằng Email, hệ thống sẽ tự động tạo Username từ Email.
   * Khi Customer đăng nhập bằng Email, chúng ta sẽ dùng thẻ <input type="email" name="username"> trong form login.
   * Tức là đây là một thủ thuật: label thì là email, nhưng <input name="username"> vẫn là username.
   * 
   * Như vậy là ta có thể tái sử dụng được hệ thống User Role đã có sẵn, không cần phải sửa đổi gì nhiều.
   * 
   * Google Gemini:
   * Có nên gộp chung Customer và Employee vào một bảng User trong Spring Boot Security
   * 
   * Role cho Customer là gì ? CUSTOMER  hay là VISITOR
   */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

}