package io.github.hoangtnedu.java.management;
@org.springframework.stereotype.Controller public class Controller {private final OrderRepository repo;private final ManagementService service;public Controller(OrderRepository r,ManagementService s){repo=r;service=s;}
 @org.springframework.web.bind.annotation.GetMapping("/") public String list(org.springframework.ui.Model m){m.addAttribute("orders",repo.findAll());return "orders";}
 @org.springframework.web.bind.annotation.PostMapping("/orders/{id}/ship") public String ship(@org.springframework.web.bind.annotation.PathVariable long id,java.security.Principal user){service.ship(id,user.getName());return "redirect:/";}
}