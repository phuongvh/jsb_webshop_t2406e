package exam.jsb_webshop_t2406e.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class AppController 
{

    @GetMapping("/app/test")
    @ResponseBody
    public String test(){
        return "Hello from App Test public";
    }
    
    // Tại sao nó lại điều hướng sang /app/test ???
    @GetMapping("/app/public")
    public String publicTest(Model model){
        return "test.html";
    }

    @GetMapping("/home")
    @ResponseBody
    public String Home()
    {
        return "Home Page";
    }

    @GetMapping("/about")
    @ResponseBody
    public String About()
    {
        return "About";
    }
}
