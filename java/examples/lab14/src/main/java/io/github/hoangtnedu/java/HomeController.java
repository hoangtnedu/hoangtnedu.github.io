package io.github.hoangtnedu.java;
@org.springframework.stereotype.Controller public class HomeController {
 @org.springframework.web.bind.annotation.GetMapping("/") public String home(){return "index";}
 @org.springframework.web.bind.annotation.GetMapping("/about") public String about(){return "about";}
 @org.springframework.web.bind.annotation.GetMapping("/login") public String login(){return "login";}
}