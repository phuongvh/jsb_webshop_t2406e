package exam.jsb_webshop_t2406e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import exam.jsb_webshop_t2406e.security.jwt.JwtUtils;


@Controller
public class AdminTestController 
{

     @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpSession session;

    @GetMapping("/admin/test")
    @ResponseBody
    public String test(){
        return "Hello from Test";
    }

    @GetMapping("/admin/bimat")
    @ResponseBody
    public String bimat(){
        return "Noi dung bi mat, phai dang nhap moi xem duoc";
    }

    @GetMapping("/admin/login")
    @ResponseBody
    public String login(){
        return "Login page under construction";
    }

    /**
     * Lỗi tiềm ẩn: Redirect Too Many Times.
     * Nguyên nhân sâu xa: thiếu thư viện ThymeLeaf.
     * 
     * Khi client truy cập vào địa chỉ localhost:8080/admin/loginform
     * thì springboot lại hiểu lầm là truy cập vào localhost:8080/admin/loginform.html
     * 
     * @param model
     * @return
     */
    @GetMapping("/admin/loginform")
    public String loginForm(Model model){
        return "loginform2";
    }

    @PostMapping("/admin/loginform")
    public String loginPost(Model model)
    {
        System.out.println("\n Đang xử lý login form");
        return "...";

        // Ok thanhfh công rồi, mặc dù nội dung của hàm này chẳng hiển thị, chẳng chạy
        // nhưng chỉ cần postmapping của nó trùng với loginform là được !!!
    }

    // https://stackoverflow.com/questions/69358200/logout-and-clear-cookies-programatically-from-controller-in-spring-boot
    // https://stackoverflow.com/questions/63939078/how-to-set-samesite-and-secure-attribute-to-jsessionid-cookie
    // import javax.servlet.http.Cookie;
    // remove user credentials
    @GetMapping("/admin/logout")
    public String logout()
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

        return "redirect:/app/public";
    }// OK, đã logout thành công !
    
}
