package com.demo;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.database.DatabaseService;
import com.demo.database.PostgresHelper;

/*
 * @Author Gabrielle Olivera
 * This controller defines several CRUD REST endpoints for reading and writing donations.
 * These backend services will handle calling to the PostgresHelper to communicate with the database.
 */
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
    public String deleteDonation(@PathVariable int id) {
        try {
            initializePgHelper();
            pgHelper.deleteDonation(id);
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

    //private methods below
    private void initializePgHelper() throws SQLException {
        //This is so pgHelper is only initialized once.
        //Initializing pgHelper only once allows for us to store some of the data in memory 
        //and limit the amount of back and forth with the database
        if(pgHelper == null) {
            pgHelper = new PostgresHelper(databaseService);
        }
    }

}
