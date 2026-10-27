package io.github.hoangtnedu.java;
import org.springframework.stereotype.Controller;import org.springframework.web.bind.annotation.*;import org.springframework.ui.Model;import org.springframework.validation.BindingResult;import jakarta.validation.Valid;
@Controller @RequestMapping("/students") public class StudentController {
 private final StudentService service;public StudentController(StudentService s){service=s;}
 @GetMapping public String list(@RequestParam(defaultValue="")String q,Model m){m.addAttribute("students",service.list(q));return "students";}
 @GetMapping("/create")public String create(Model m){m.addAttribute("student",new Student());return "form";}
 @GetMapping("/edit/{id}")public String edit(@PathVariable long id,Model m){m.addAttribute("student",service.get(id));return "form";}
 @PostMapping("/save")public String save(@Valid @ModelAttribute("student") Student student,BindingResult errors){if(errors.hasErrors())return "form";try{service.save(student);}catch(IllegalArgumentException ex){errors.rejectValue("studentCode","duplicate",ex.getMessage());return "form";}return "redirect:/students";}
 @PostMapping("/delete/{id}")public String delete(@PathVariable long id){service.delete(id);return "redirect:/students";}
}