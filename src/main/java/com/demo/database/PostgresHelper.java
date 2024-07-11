package com.demo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.demo.Donation;

/*
 * This class handles connecting to and communicating with the database.
 */

public class PostgresHelper {
    //TODO: Put this in application properties
    private static final String url = "jdbc:postgresql://localhost:5432/donation_demo";
    private static final String user = "postgres";
    private static final String password = "niko23Olive";
    private static final String LIMIT = "100";

    private Connection dbConnection;

    public PostgresHelper() throws SQLException {
        //Constructor connects to the database
        try {
            dbConnection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("There was an error connecting to the database.");
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<Donation> getAllDonations() throws SQLException {
        ArrayList<Donation> donations = new ArrayList<Donation>();
        try {
            PreparedStatement query = dbConnection.prepareStatement(
                "SELECT donation.id, donor.first_name, donor.last_name, dtype.name, donation.quantity, donation.date"
                + " FROM public.donation donation"
	            + " JOIN public.donor donor ON donor.id = donation.donor_id"
	            + " JOIN public.donation_type dtype ON dtype.id = donation.type_id"
                + " LIMIT " + LIMIT + ";"
            );
            ResultSet results = query.executeQuery();

            while(results.next()) {
                Donation donation = new Donation(
                    results.getString("first_name"),
                    results.getString("last_name"),
                    results.getString("quantity"),
                    results.getString("name"),
                    results.getDate("date")
                );
                donation.setId(results.getInt("id"));
                donations.add(donation);
            }
        } catch (SQLException e) {
            System.out.println("There was an error connecting to the database.");
            e.printStackTrace();
            throw e;
        }
        return donations;
    }

}