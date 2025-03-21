package exam.jsb_webshop_t2406e.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CustomerTestController 
{
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

    @PostMapping("/customer/loginpost")
    public String loginPost(Model model)
    {
        System.out.println("\n Đang xử lý login form khách hàng");
        return "loginpost-customer.html";

        // Ok thanhfh công rồi, mặc dù nội dung của hàm này chẳng hiển thị, chẳng chạy
        // nhưng chỉ cần postmapping của nó trùng với loginform là được !!!
    }
    
}
