package com.example.globallogic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;




@RestController
@RequestMapping(path="")
public class MyController {

	private final MyService service;
	
	@Autowired
	public MyController(MyService service) {
		this.service = service;
	}
	
	@GetMapping(path = "globallogic/v1/tests")
	public List<GlobalLogic> getTests() {
		return service.getTests();
	}
	
	@PostMapping(path = "globallogic/v1/tests")
	public List<GlobalLogic> post(@RequestBody MultiValueMap<String, String> formData) {
		return service.analizeFormData(formData);
	}
	
	@GetMapping
	public ModelAndView welcome() {
		ModelAndView modelAndView = new ModelAndView("form");
		return modelAndView;
	}
	
	
	
}
