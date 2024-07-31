package com.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demo.database.DatabaseService;
import com.demo.database.PostgresHelper;

/*
 * @Author Gabrielle Olivera
 * This class tests the web service API endpoints.
 */

@ExtendWith(MockitoExtension.class)

public class DontationWebServiceControllerTest {
    @Mock
    private DatabaseService databaseService;

    @Mock
    private PostgresHelper pgHelper;

    @InjectMocks
    private DonationWebServiceController donationWebServiceController;


    @Test
    void getAllDonationsShouldReturnListOfDonations() throws Exception {
        //Given
        ArrayList<Donation> donations = new ArrayList<Donation>();
        Donation donation1 = new Donation("Joe", "Black", "$50", "money", Date.valueOf("2024-01-01"));
        Donation donation2 = new Donation("Jill", "Green", "2 cans", "canned food", Date.valueOf("2024-01-02"));
        donations.add(donation1);
        donations.add(donation2);

        given(pgHelper.getAllDonations()).willReturn(donations);

        //When
        ArrayList<Donation> results = donationWebServiceController.getAllDonations();

        //Then
        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    void createNewDonationShouldHandleSuccess() throws Exception {
        Donation donation1 = new Donation("Joe", "Black", "$50", "money", Date.valueOf("2024-01-01"));
       
        String result = donationWebServiceController.createNewDonation(donation1);
        
        //verifies that PostgresHelper.insertDonation is called one time.
        verify(pgHelper, times(1)).insertDonation(donation1);

        assertThat(result).isEqualTo("Success");
    }

    @Test
    void createNewDonationShouldHandleErrors() throws Exception {
        Donation donation1 = new Donation("Joe", "Black", "$50", "money", Date.valueOf("2024-01-01"));
        doThrow(new SQLException()).when(pgHelper).insertDonation(donation1);
        
        String result = donationWebServiceController.createNewDonation(donation1);
        
        //verifies that PostgresHelper.insertDonation is called one time.
        verify(pgHelper, times(1)).insertDonation(donation1);

        //verifies that exception is handled
        assertThat(result).isEqualTo("Error");
    }

    @Test
    void updateDonationShouldHandleSuccess() throws Exception {
        Donation donation1 = new Donation("Joe", "Black", "$50", "money", Date.valueOf("2024-01-01"));
       
        String result = donationWebServiceController.updateDonation(donation1);
        
        //verifies that PostgresHelper.insertDonation is called one time.
        verify(pgHelper, times(1)).updateDonation(donation1);

        assertThat(result).isEqualTo("Success");
    }

    @Test
    void updateDonationShouldHandleErrors() throws Exception {
        Donation donation1 = new Donation("Joe", "Black", "$50", "money", Date.valueOf("2024-01-01"));
        doThrow(new SQLException()).when(pgHelper).updateDonation(donation1);
        
        String result = donationWebServiceController.updateDonation(donation1);
        
        //verifies that PostgresHelper.insertDonation is called one time.
        verify(pgHelper, times(1)).updateDonation(donation1);

        //verifies that exception is handled
        assertThat(result).isEqualTo("Error");
    }

    @Test
    void deleteDonationShouldHandleSuccess() throws Exception {
        int donationId = 1234;
        String result = donationWebServiceController.deleteDonation(donationId);
        
        //verifies that PostgresHelper.insertDonation is called one time.
        verify(pgHelper, times(1)).deleteDonation(donationId);

        assertThat(result).isEqualTo("Success");
    }

    @Test
    void deleteDonationShouldHandleErrors() throws Exception {
        int donationId = 1234;
        doThrow(new SQLException()).when(pgHelper).deleteDonation(donationId);
        
        String result = donationWebServiceController.deleteDonation(donationId);
        
        //verifies that PostgresHelper.insertDonation is called one time.
        verify(pgHelper, times(1)).deleteDonation(donationId);

        //verifies that exception is handled
        assertThat(result).isEqualTo("Error");
    }

    @Test
    void getDonationShouldReturnDonation() throws Exception {
        Donation donation1 = new Donation("Joe", "Black", "$50", "money", Date.valueOf("2024-01-01"));
        donation1.setId(1234);

        given(pgHelper.getDonation(1234)).willReturn(donation1);

        Donation result = donationWebServiceController.getDonation(donation1.getId());

        assertThat(result.getId()).isEqualTo(donation1.getId());
    }

    @Test
    void getDonationShouldReturnEmptyDonationOnError() throws Exception {
        int donationId = 1234;
        doThrow(new SQLException()).when(pgHelper).getDonation(donationId);
        
        Donation result = donationWebServiceController.getDonation(donationId);

        //verifies an empty Donation record is returned when an exception is thrown
        assertThat(result.getId()).isEqualTo(0);
    }

    
    
}
