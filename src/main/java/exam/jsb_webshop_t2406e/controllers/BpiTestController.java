package exam.jsb_webshop_t2406e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import exam.jsb_webshop_t2406e.security.jwt.JwtUtils;

import org.springframework.web.bind.annotation.RequestParam;


// Back End API specifically designed for same domain request (restman, postman)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/bpi/test")
public class BpiTestController 
{
    @Autowired
  JwtUtils jwtUtils;

  
  @GetMapping("/public")
  public String testPublicContent(){
    return "Test Content public to users, very public !!!";
  }

  @GetMapping("/all")
  public String allAccess() {
    return "Public Content for All to Access.";
  }

  /* Nếu truy cập vào BPI đòi hỏi quyền cụ thể, thì nó sẽ báo lỗi:
   * {
   *   "statusCode":500,
   *   "timestamp":"2025-04-10T08:35:42.025+00:00",
   *   "message":"An Authentication object was not found in the SecurityContext",
   *   "description":"uri=/bpi/test/user"
   * }
   * 
   * Điều này có nghĩa là bạn phải đăng nhập vào trước, rồi mới gọi được.
   */
  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/tolink")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String redirectTest() {
    return "redirect:https://vnexpress.net";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }
}
