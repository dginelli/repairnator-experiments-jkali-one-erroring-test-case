package com.kh013j.controllers.admin;

import com.kh013j.controllers.util.ViewName;
import com.kh013j.model.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OrderAdminController {
    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/admin/order/all", method = RequestMethod.GET)
    public ModelAndView showUsers() {
        return new ModelAndView(ViewName.SHOW_ORDERS, "Orders", orderService.findAll());
    }
}
