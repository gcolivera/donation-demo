package com.demo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.database.DatabaseService;
import com.demo.database.PostgresHelper;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class DonationWebServiceController {
    @Autowired
    private DatabaseService databaseService;

    private PostgresHelper pgHelper = null;

    //Backend REST Services paths are defined here
	@GetMapping("/donations")
	public ArrayList<Donation> getAllDonations() {
		ArrayList<Donation> donations = new ArrayList<Donation>();
		try {
			initializePgHelper();
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
	public String createNewDonation(@RequestBody Donation donation) {
		try {
			initializePgHelper();
			pgHelper.insertDonation(donation);			
		} catch (Exception e) {
			System.out.println("An error happened when connecting to database");
			return "Error";
		}
        return "Success";
	}

    @PutMapping("/donation")
    public String updateDonation(@RequestBody Donation donation) {
        try {
            initializePgHelper();
            pgHelper.updateDonation(donation);
        } catch (SQLException e) {
            return "Error";
        }
        
        return "Success";
    }

    @DeleteMapping("/donation/{id}")
    public String deleteDontation(@PathVariable int id) {
        try {
            initializePgHelper();
            pgHelper.deleteDontation(id);
        } catch (SQLException e) {
            return "Error";
        }
        return "Success";
    }

    @GetMapping("/donation/{id}")
    public Donation getDonation(@PathVariable int id) {
        try {
            initializePgHelper();
            Donation donation = pgHelper.getDonation(id);
            return donation;
        } catch(SQLException e) {
            return new Donation();
        }
    }

    //private method
    private void initializePgHelper() throws SQLException {
        if(pgHelper == null) {
            pgHelper = new PostgresHelper(databaseService);
        }
    }

}
