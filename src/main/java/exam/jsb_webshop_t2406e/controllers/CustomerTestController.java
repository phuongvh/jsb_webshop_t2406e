package exam.jsb_webshop_t2406e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.jsb_webshop_t2406e.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerTestController 
{
    @Autowired
    JwtUtils jwtUtils; // dòng này là bắt buộc, nếu không sẽ bị lỗi 403 khi submit Form Login !!!

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpSession session;

    @GetMapping("/customer/test")
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
    
}
