package ie.ucd.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
  @Autowired

  @GetMapping("/admin/dashboard")
  public String indexView() {
    return "admin/index.html";
  }
}