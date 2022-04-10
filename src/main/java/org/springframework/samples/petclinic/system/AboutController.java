package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class AboutController {

	@GetMapping("/about")
	public String about() {
		return "about";
	}

}
