package com.demo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.demo.Donation;

/*
 * @Author Gabrielle Olivera
 * This class handles connecting to and communicating with the database.
 * The code is very specific to Postgresql JDBC. 
 * If this application was switched to another type of database or datastore
 * a new class can be written to replace this one to interact with that datastore.
 */

public class PostgresHelper {
    private static Logger logger = Logger.getLogger(PostgresHelper.class.getName());
    private static final String LIMIT = "100";
    
    //Keeping some hashmaps in cache to avoid running queries
    private HashMap<String, Integer> donationTypes;
    private HashMap<String, Integer> donorsMap;
    private HashMap<Integer, Donation> donationMap;

    //This service is needed to keep database specific infomation in the application.properties file
    private DatabaseService databaseService;

    private Connection dbConnection;

    public PostgresHelper(DatabaseService databaseService) throws SQLException {
        this.donationTypes = new HashMap<String, Integer>();
        this.donorsMap = new HashMap<String, Integer>();
        this.donationMap = new HashMap<Integer, Donation>();
        this.databaseService = databaseService;
    }

    public ArrayList<Donation> getAllDonations() throws SQLException {
        ArrayList<Donation> donations = new ArrayList<Donation>();
        try {
            openConnection();
            PreparedStatement query = dbConnection.prepareStatement(
                "SELECT donation.id, donor.first_name, donor.last_name, donor.id as donor_id, dtype.id as type_id,"
                + " dtype.name as type_name, donation.quantity, donation.date"
                + " FROM public.donation donation"
	            + " JOIN public.donor donor ON donor.id = donation.donor_id"
	            + " JOIN public.donation_type dtype ON dtype.id = donation.type_id"
                + " ORDER BY donation.date DESC, donor.last_name, donor.first_name"
                + " LIMIT " + LIMIT + ";"
            );
            ResultSet results = query.executeQuery();

            while(results.next()) {
                Donation donation = new Donation(
                    results.getString("first_name"),
                    results.getString("last_name"),
                    results.getString("quantity"),
                    results.getString("type_name"),
                    results.getDate("date")
                );
                donation.setId(results.getInt("id"));
                donations.add(donation);
                donationMap.put(donation.getId(), donation);
                donorsMap.put(donation.getFirstName() + ' ' + donation.getLastName(), results.getInt("donor_id"));
            }
            dbConnection.close();
        } catch (SQLException e) {
            closeConnectionAndLogError("Could not pull donation list.", e);
            throw e;
        }
        return donations;
    }

    public void insertDonation(Donation donation) throws SQLException {
        getDonationTypes();
        int donorTypeId = donationTypes.get(donation.getType());
        int donorId = createDonor(donation.getFirstName(), donation.getLastName());
        try {
            openConnection();
            PreparedStatement query = dbConnection.prepareStatement(
                "INSERT INTO donation (donor_id, type_id, quantity, date) " +
                "VALUES (?, ?, ?, ?);"
            );
            query.setInt(1, donorId);
            query.setInt(2, donorTypeId);
            query.setString(3, donation.getQuantity());
            query.setDate(4, donation.getDate());
            query.executeUpdate();
            dbConnection.close();
        } catch(SQLException e) {
            closeConnectionAndLogError("Could not create donation.", e);
            throw e;
        }
    }

    public HashMap<String, Integer> getDonationTypes() {
        if(donationTypes.size() > 0) {
            return donationTypes;
        }
        try {
            openConnection();
            PreparedStatement query = dbConnection.prepareStatement(
             "SELECT id, name FROM donation_type;"
            );

            ResultSet results = query.executeQuery();

            while(results.next()) {
                donationTypes.put(results.getString("name"), results.getInt("id"));
            }
            dbConnection.close();

        } catch(SQLException e) {
            closeConnectionAndLogError("Could not get list of donation types from database.", e);
        }
        return donationTypes;
    }

    public void updateDonation(Donation donation) throws SQLException {
        try {
            getDonationTypes();
            int donorTypeId = donationTypes.get(donation.getType());
            int donorId = createDonor(donation.getFirstName(), donation.getLastName());

            openConnection();
            PreparedStatement query = dbConnection.prepareStatement(
                "UPDATE donation SET donor_id = ?, type_id = ?, quantity = ?, date=?"
                + " WHERE id = ?;"
            );
            query.setInt(1, donorId);
            query.setInt(2, donorTypeId);
            query.setString(3, donation.getQuantity());
            query.setDate(4, donation.getDate());
            query.setInt(5, donation.getId());
            query.executeUpdate();
            dbConnection.close();
        } catch(SQLException e) {
            closeConnectionAndLogError("Could not update donation.", e);
            throw e;
        }
    }

    public void deleteDonation(int id) throws SQLException {
        //TODO: Future enhancement to delete donor if there are no more donations
        try {
            openConnection();
            PreparedStatement query = dbConnection.prepareStatement(
                "DELETE FROM donation WHERE id = ?;"
            );

            query.setInt(1, id);
            query.executeUpdate();
            dbConnection.close();
        } catch(SQLException e) {
            closeConnectionAndLogError("Could not delete donation.", e);
            throw e;
        }
    }

    public Donation getDonation(int id) throws SQLException {
        if(donationMap.containsKey(id)) {
            return donationMap.get(id);
        }
        //else query for it
        try {
            openConnection();
            Donation donation = new Donation();
            PreparedStatement query = dbConnection.prepareStatement(
                "SELECT donation.id, donor.first_name, donor.last_name,"
                + " dtype.name, donation.quantity, donation.date"
                + " FROM public.donation donation"
	            + " JOIN public.donor donor ON donor.id = donation.donor_id"
	            + " JOIN public.donation_type dtype ON dtype.id = donation.type_id"
                + " WHERE donation.id = ?;"
            );
            query.setInt(1, id);

            ResultSet results = query.executeQuery();

            while(results.next()) {
                donation.setFirstName(results.getString("first_name"));
                donation.setLastName(results.getString("last_name"));
                donation.setQuantity(results.getString("quantity"));
                donation.setType(results.getString("name"));
                donation.setDate(results.getDate("date"));
                donation.setId(results.getInt("id"));
            }
            dbConnection.close();
            return donation;

        } catch(SQLException e) {
            closeConnectionAndLogError("Could not get donor from id.", e);
            throw e;
        }
    }

    //private helper methods

    private void openConnection() throws SQLException {
        try {
            dbConnection = DriverManager.getConnection(databaseService.getUrl(), databaseService.getUser(), databaseService.getPassword());
        } catch (SQLException e) {
            System.out.println("There was an error connecting to the database.");
            e.printStackTrace();
            throw e;
        }
    }

    private void closeConnectionAndLogError(String customMessage, SQLException e) {
        logger.log(Level.FINE, customMessage);
        e.printStackTrace();

        try {
            if(dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (SQLException e2) {
            logger.log(Level.FINE, "Could not close connection.");
            e2.printStackTrace();
        }
    }

    private int createDonor(String firstName, String lastName) throws SQLException {
        int id = findDonorId(firstName, lastName);
        if (id != 0) {
            return id;
        }
        //Otherwise create donor
        try {
            openConnection();
            PreparedStatement query = dbConnection.prepareStatement(
             "INSERT INTO donor (first_name, last_name) VALUES (?, ?);"
            );
            query.setString(1, firstName);
            query.setString(2, lastName);
            query.executeUpdate();
            
            id = findDonorId(firstName, lastName);
            dbConnection.close();
        } catch(SQLException e) {
            closeConnectionAndLogError("Could not create donor.", e);
            throw e;
        }

        return id;
    }

    private int findDonorId(String firstName, String lastName) {
        String fullName = firstName + " " + lastName;
        //Caching a donorMap to limit the amount of queries
        if (donorsMap.containsKey(fullName)) {
            return donorsMap.get(fullName);
        }
        try {
            openConnection();
            PreparedStatement query = dbConnection.prepareStatement(
             "SELECT id FROM donor WHERE first_name = '" + firstName + "'"
             + "AND last_name = '" + lastName + "' LIMIT 1;"
            );
            ResultSet results = query.executeQuery();
            while(results.next()) {
                //Should only be one value
                int id = results.getInt("id");
                if(id > 0) {
                    donorsMap.put(fullName, id);
                }
                return id;
            }
            dbConnection.close();
        } catch(SQLException e) {
            closeConnectionAndLogError("Could not find donor in database.", e);
        }
        return 0;
    }
}