package com.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DonationFormController {

	@GetMapping("/")
	public String home(Model model) {
		return "donationList";
	}

	@GetMapping("/donation-form")
	public String donationForm(Model model) {
		model.addAttribute("donation", new Donation());
		return "donationForm";
	}

	@PostMapping("/donation-form")
	public String donationFormSubmit(@ModelAttribute Donation donation, Model model) {
		model.addAttribute("donation", donation);
		return "donationlist";
	}

}
