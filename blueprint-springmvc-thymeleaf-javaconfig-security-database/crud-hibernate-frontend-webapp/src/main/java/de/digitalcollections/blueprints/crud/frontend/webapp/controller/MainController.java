package de.digitalcollections.blueprints.crud.frontend.webapp.controller;

import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = {"", "/"})
public class MainController {

  @RequestMapping(method = RequestMethod.GET)
  public String printWelcome(Model model) {
    model.addAttribute("time", new Date());
    return "main";
  }

  @RequestMapping("/login")
  public String login(@RequestParam(value = "error", defaultValue = "false") boolean error, Model model) {
    model.addAttribute("error", error);
    model.addAttribute("login", true);
    return "login";
  }
}
