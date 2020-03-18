package de.digitalcollections.blueprints.crud.frontend.webapp.controller;

import de.digitalcollections.blueprints.crud.business.api.service.UserService;
import de.digitalcollections.blueprints.crud.model.api.security.User;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * External REST-Interfaces
 */
@Controller
@RequestMapping("/api")
public class ApiController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

  @Autowired
  private UserService userService;

  @ResponseBody
  @RequestMapping(value = "users", method = RequestMethod.GET)
  public List<User> getUsers() {
    return userService.getAll();
  }
}
