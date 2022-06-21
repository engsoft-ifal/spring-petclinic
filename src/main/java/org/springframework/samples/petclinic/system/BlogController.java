package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class BlogController {

	@GetMapping("/blog")
	public String blog() {
		return "blog";
	}

}
