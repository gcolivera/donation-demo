package com.demo;

import java.util.ArrayList;
import java.sql.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.database.PostgresHelper;

@SpringBootApplication
@RestController
public class DonationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonationApplication.class, args);
	}

	//Backend REST Services paths are defined here

	@GetMapping("/hello")
	public String hello(@RequestParam(value="name", defaultValue="World") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/donations")
	public ArrayList<Donation> getAllDonations() {
		ArrayList<Donation> donations = new ArrayList<Donation>();
		
		
		try {
			PostgresHelper pgHelper = new PostgresHelper();
			donations.addAll(pgHelper.getAllDonations());
			return donations;
		} catch (Exception e) {
			System.out.println("No donations found in database");
		}
		//If cannot connect to database use test donations
		Donation joe = new Donation("Joe", "Black", "$200", "money", Date.valueOf("2024-07-01"));
		Donation betty = new Donation("Betty", "White", "$200", "money", Date.valueOf("2024-06-01"));
		Donation sue = new Donation("Sue", "Brown", "$200", "money", Date.valueOf("2023-12-24"));
		Donation mary = new Donation("Mary", "Rose", "$200", "money", Date.valueOf("2024-05-01"));
		donations.add(joe);
		donations.add(betty);
		donations.add(sue);
		donations.add(mary);
		return donations;
	}

	@PostMapping("/donation")
	public String createNewDonation() {
		return "Success";
	}


}
