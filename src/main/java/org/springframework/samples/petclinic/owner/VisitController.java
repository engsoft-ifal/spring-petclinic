/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.samples.petclinic.vet.Day;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 */
@Controller
class VisitController {

	private final OwnerRepository owners;

	private final VisitRepository visits;

	private final VetRepository vets;

	public VisitController(OwnerRepository _owners, VisitRepository _visits, VetRepository _vets) {
		this.owners = _owners;
		this.visits = _visits;
		this.vets = _vets;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @RequestMapping annotated method. 2 goals: - Make sure
	 * we always have fresh data - Since we do not use the session scope, make sure that
	 * Pet object always has an id (Even though id is not part of the form fields)
	 * @param petId
	 * @return Pet
	 */
	@ModelAttribute("visit")
	public Visit loadPetWithVisit(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			Map<String, Object> model) {
		Owner owner = this.owners.findById(ownerId);
		Pet pet = owner.getPet(petId);
		model.put("pet", pet);
		model.put("owner", owner);
		Visit visit = new Visit();
		pet.addVisit(visit);
		return visit;
	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is
	// called
	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String firstStepNewVisitForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return "pets/createOrUpdateVisitForm";
	}

	// @PostMapping("/owners/{ownerId}/pets/{petId}/visits/new/2")
	// public String secondStepNewVisitForm(@PathVariable("petId") int petId, Map<String,
	// Object> model) {
	// return "pets/createOrUpdateVisitForm";
	// }

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is
	// called
	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(@ModelAttribute Owner owner, @PathVariable int petId, @Valid Visit visit,
			BindingResult result) {

		Calendar cal = Calendar.getInstance();
		Date date1 = Date.from(visit.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
		cal.setTime(date1);
		String[] daysOfWeek = { "Domingo", "Segunda-feira", "Terca-feira", "Quarta-feira", "Quinta-feira",
				"Sexta-feira", "Sabado" };
		String selectedDay = daysOfWeek[cal.get(Calendar.DAY_OF_WEEK) - 1];
		AvailableDays availableDayId = visits.findByDayName(selectedDay);

		return "redirect:/owners/" + owner.getId() + "/pets/" + petId + "/visits/new/" + availableDayId.getId() + "/"
				+ visit.getDate().toString();

		// if (result.hasErrors()) {
		// return "pets/createOrUpdateVisitForm";
		// }
		// else {
		// owner.addVisit(petId, visit);
		// this.owners.save(owner);
		// return "redirect:/owners/{ownerId}";
		// }
	}

	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/new/{availableDayId}/{selectedDate}")
	private String getNewVisitFormPartTwo(Model model, @ModelAttribute Owner owner, @PathVariable int petId,
			@PathVariable int availableDayId, @PathVariable String selectedDate) {
		Collection<Vet> vets = this.vets.findAll();
		Day day = this.vets.findDayById(availableDayId);
		List<Vet> availableVets = new ArrayList<Vet>();

		for (Vet vet : vets) {
			List<Day> vetDays = vet.getDays();
			for (Day dayVet : vetDays) {
				if (dayVet.getName().equals(day.getName())) {
					availableVets.add(vet);
					break;
				}
			}
		}

		model.addAttribute("availableVets", availableVets);
		return "pets/createOrUpdateVisitForm2";
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/new/{availableDayId}/{selectedDate}")
	public String processNewVisitFormPartTwo(@ModelAttribute("visitForm") String vetName, @ModelAttribute Owner owner,
			@PathVariable int petId, @Valid Visit visit, @PathVariable String selectedDate, BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm2";
		}
		else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate convertedSelectDate = LocalDate.parse(selectedDate, formatter);
			visit.setDate(convertedSelectDate);
			Vet vet = vets.findByName(vetName);
			visit.setVet(vet);
			owner.addVisit(petId, visit);
			this.owners.save(owner);
			return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/{visitId}/edit")
	public ModelAndView initEditVisitForm(@PathVariable("visitId") int visitId) {
		ModelAndView mav = new ModelAndView("pets/createOrUpdateVisitForm");
		Visit visit = this.visits.findById(visitId);
		mav.addObject(visit);
		return mav;
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/{visitId}/edit")
	public String editVisitForm(@ModelAttribute Owner owner, @PathVariable int petId, @PathVariable int visitId,
			@Valid Visit visit, BindingResult result) {
		Calendar cal = Calendar.getInstance();
		Date date1 = Date.from(visit.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
		cal.setTime(date1);
		String[] daysOfWeek = { "Domingo", "Segunda-feira", "Terca-feira", "Quarta-feira", "Quinta-feira",
				"Sexta-feira", "Sabado" };
		String selectedDay = daysOfWeek[cal.get(Calendar.DAY_OF_WEEK) - 1];
		AvailableDays availableDayId = visits.findByDayName(selectedDay);

		return "redirect:/owners/" + owner.getId() + "/pets/" + petId + "/visits/" + visitId + "/edit/"
				+ availableDayId.getId() + "/" + visit.getDate().toString();
	}

	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/{visitId}/edit/{availableDayId}/{selectedDate}")
	private String getEditVisitFormPartTwo(Model model, @ModelAttribute Owner owner, @PathVariable int petId,
			@PathVariable int visitId, @PathVariable int availableDayId, @PathVariable String selectedDate) {
		Visit visit = this.visits.findById(visitId);

		Collection<Vet> vets = this.vets.findAll();
		Day day = this.vets.findDayById(availableDayId);
		List<Vet> availableVets = new ArrayList<Vet>();

		for (Vet vet : vets) {
			List<Day> vetDays = vet.getDays();
			for (Day dayVet : vetDays) {
				if (dayVet.getName().equals(day.getName())) {
					availableVets.add(vet);
					break;
				}
			}
		}

		model.addAttribute("visit", visit);
		model.addAttribute("availableVets", availableVets);
		return "pets/createOrUpdateVisitForm2";
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/{visitId}/edit/{availableDayId}/{selectedDate}")
	public String editVisitFormPartTwo(@ModelAttribute("visitForm") String vetName, @ModelAttribute Owner owner,
			@PathVariable int petId, @Valid Visit visit, @PathVariable int visitId, @PathVariable String selectedDate,
			BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm2";
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate convertedSelectDate = LocalDate.parse(selectedDate, formatter);
		visit.setDate(convertedSelectDate);
		visit.setId(visitId);

		this.visits.save(visit);
		return "redirect:/owners/{ownerId}";
	}

}
