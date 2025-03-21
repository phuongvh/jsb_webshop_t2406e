package exam.jsb_webshop_t2406e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.jsb_webshop_t2406e.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerTestController 
{
    @Autowired
    JwtUtils jwtUtils; // dòng này là bắt buộc, nếu không sẽ bị lỗi 403 khi submit Form Login !!!
                        // Nếu mà không có jwtUtils thì phải có dòng lệnh trong AuthTokenFilter để nó bỏ qua
                        // Đại ý là Controller sẽ thất bại trong việc gửi JWT kèm theo response về phía khách Client
                        // Hậu quả là: Sau Khi login form submit vẫn chết: lỗi 403, 500

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpSession session;

    @GetMapping({
        "/customer/test",
        "/customer/public"
    })
    @ResponseBody
    public String test(){
        return "Hello from customer test public content";
    }

    @GetMapping("/customer/bimat")
    // @PreAuthorize("hasRole('USER')") // vẫn failed
    @ResponseBody
    public String bimat(){
        return "Day la noi dung bi mat, danh cho khach hang";
    }

    @GetMapping("/customer/login")
    public String login(){
        return "login-customer.html";
    }
    // chạy OK get Login Form Customer

    // Spring Security đòi hỏi là @Get và @Post phải có chung URL
    // Nếu khác thì AuthTokenFilter và các hàm filterChain() sẽ không hoạt động đúng như mình kỳ vọng
    @PostMapping("/customer/login")
    public String loginPost(Model model)
    {
        System.out.println("\n Đang xử lý login form khách hàng");
        return "loginpost-customer.html";

        // Ok thanhfh công rồi, mặc dù nội dung của hàm này chẳng hiển thị, chẳng chạy
        // nhưng chỉ cần postmapping của nó trùng với loginform là được !!!
    }
    
    // https://stackoverflow.com/questions/69358200/logout-and-clear-cookies-programatically-from-controller-in-spring-boot
    // https://stackoverflow.com/questions/63939078/how-to-set-samesite-and-secure-attribute-to-jsessionid-cookie
    // import javax.servlet.http.Cookie;
    // remove user credentials
    @GetMapping("/customer/logout")
    public String logoutCustomer()
    {
        // jwtUtils.getCleanJwtCookie();
        // return "logout.html";

        boolean isSecure = false;
        String contextPath = null;
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            isSecure = request.isSecure();
            contextPath = request.getContextPath();
        }

        SecurityContext context = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        context.setAuthentication(null);

        if (response != null) {
            Cookie cookie = new Cookie("JSESSIONID", null);
            String cookiePath = StringUtils.hasText(contextPath) ? contextPath : "/";
            cookie.setPath(cookiePath);
            cookie.setMaxAge(0);
            cookie.setSecure(isSecure);
            response.addCookie(cookie);
        }

        return "redirect:/customer/test";
    }// OK, đã logout thành công !
}// end class
