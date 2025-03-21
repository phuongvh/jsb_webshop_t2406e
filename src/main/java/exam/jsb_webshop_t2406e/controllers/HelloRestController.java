package exam.jsb_webshop_t2406e.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class HelloRestController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }
}


