package com.demo;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class DonationFormController {
	//TODO put this in properties
	private String rootUrl = "http://localhost:8080";

	@GetMapping("/")
	public String home(Model model) {
		return "donationList";
	}

	@GetMapping("/donation-form")
	public String donationForm(@RequestParam(name="id", required=false, defaultValue="0") int id, Model model) {
		if(id == 0) {
			model.addAttribute("donation", new Donation());
		} else {
			String uri = rootUrl + "/donation/" + id;
			RestTemplate restTemplate = new RestTemplate();
			Donation donation = restTemplate.getForObject(uri, Donation.class);
			model.addAttribute("donation", donation);
		}

		return "donationForm";
	}

	@PostMapping("/donation-form")
	public String donationFormSubmit(@ModelAttribute Donation donation, Model model) {
		model.addAttribute("donation", donation);
		String uri = rootUrl + "/donation";
		RestTemplate restTemplate = new RestTemplate();
		String response;
		if(donation.getId() != 0) {
			//If there is an id then the donation is being edited
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("id", Integer.toString(donation.getId()));
			restTemplate.put(uri, donation, params);
			response = "Success";

		} else {
			//If there is not an id then the donation is being created
			response = restTemplate.postForObject(uri, donation, String.class);
		}
		if (response == "Error") {
			return "error";
		}

		return "submit";
	}

}
