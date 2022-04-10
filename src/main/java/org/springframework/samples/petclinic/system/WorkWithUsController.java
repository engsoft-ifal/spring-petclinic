package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class WorkWithUsController {

	@GetMapping("/work-with-us")
	public String workWithUs() {
		return "workWithUs";
	}

}
