package com.model.util;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping()
public class HelloController {
//	@RequestMapping(value = "/",method = RequestMethod.GET)
//	public String printWelcome(ModelMap model) {
//		model.addAttribute("message", "Hello world!");
//		return "admin-login";
//	}
	@RequestMapping(value = "/admin-login",method = RequestMethod.GET)
	public String login(ModelMap model) {
		return "admin-login";
	}

	@RequestMapping(value = "/main",method = RequestMethod.GET)
	public String mainFrame(ModelMap model) {
		return "admin-table";
	}
}