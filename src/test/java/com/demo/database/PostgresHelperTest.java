package com.demo.database;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.demo.Donation;

@ExtendWith(MockitoExtension.class)
public class PostgresHelperTest {
    @Mock
    private DatabaseService databaseService;

    private Connection dbConnection;
    private MockedStatic<DriverManager> driverManager;

    @InjectMocks
    private PostgresHelper pgHelper;

    @BeforeEach
    public void setup() throws SQLException {
        when(databaseService.getUrl()).thenReturn("url");
        when(databaseService.getPassword()).thenReturn("password");
        when(databaseService.getUser()).thenReturn("username");

        driverManager = mockStatic(DriverManager.class);
        dbConnection = mock(Connection.class);
        driverManager.when(() -> DriverManager.getConnection("url", "username", "password")).thenReturn(dbConnection);
    }

    @AfterEach
    public void shutDown() {
        driverManager.close();
    }

    @Test
    public void getAllDonationsShouldReturnListOfDonations() throws Exception {
        //Given
        ResultSet resultSet = mock(ResultSet.class);
        String firstName = "Joe";
        String lastName = "Black";
        int id = 1234;

        when(resultSet.next()).thenReturn(true).thenReturn(false);

        when(resultSet.getString("first_name")).thenReturn(firstName);
        when(resultSet.getString("last_name")).thenReturn(lastName);
        when(resultSet.getString("quantity")).thenReturn("$50");
        when(resultSet.getString("type_name")).thenReturn("money");
        when(resultSet.getDate("date")).thenReturn(Date.valueOf("2024-01-01"));
        when(resultSet.getInt("id")).thenReturn(id);

        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(dbConnection.prepareStatement( "SELECT donation.id, donor.first_name, donor.last_name, donor.id as donor_id, dtype.id as type_id,"
            + " dtype.name as type_name, donation.quantity, donation.date"
            + " FROM public.donation donation"
            + " JOIN public.donor donor ON donor.id = donation.donor_id"
            + " JOIN public.donation_type dtype ON dtype.id = donation.type_id"
            + " ORDER BY donation.date DESC, donor.last_name, donor.first_name"
            + " LIMIT " + 100 + ";")).thenReturn(preparedStatement);

        ArrayList<Donation> results = pgHelper.getAllDonations();

        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getFirstName()).isEqualTo(firstName);
        assertThat(results.get(0).getLastName()).isEqualTo(lastName);
        assertThat(results.get(0).getId()).isEqualTo(id);

        //verify that dbConnection gets closed
        verify(dbConnection, times(1)).close();
    }

    @Test
    public void getAllDonationsNoDonations() throws Exception {
        //Given
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.next()).thenReturn(false);

        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(dbConnection.prepareStatement( "SELECT donation.id, donor.first_name, donor.last_name, donor.id as donor_id, dtype.id as type_id,"
            + " dtype.name as type_name, donation.quantity, donation.date"
            + " FROM public.donation donation"
            + " JOIN public.donor donor ON donor.id = donation.donor_id"
            + " JOIN public.donation_type dtype ON dtype.id = donation.type_id"
            + " ORDER BY donation.date DESC, donor.last_name, donor.first_name"
            + " LIMIT " + 100 + ";")).thenReturn(preparedStatement);

        ArrayList<Donation> results = pgHelper.getAllDonations();

        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(0);

        //verify that dbConnection gets closed
        verify(dbConnection, times(1)).close();
    }

    @Test
    public void getAllDonationsErrorHandling() throws Exception {
        //Given
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(dbConnection.prepareStatement( "SELECT donation.id, donor.first_name, donor.last_name, donor.id as donor_id, dtype.id as type_id,"
            + " dtype.name as type_name, donation.quantity, donation.date"
            + " FROM public.donation donation"
            + " JOIN public.donor donor ON donor.id = donation.donor_id"
            + " JOIN public.donation_type dtype ON dtype.id = donation.type_id"
            + " ORDER BY donation.date DESC, donor.last_name, donor.first_name"
            + " LIMIT " + 100 + ";")).thenReturn(preparedStatement);
        
        doThrow(new SQLException()).when(preparedStatement).executeQuery();       
        

        assertThrows(SQLException.class, () -> {
            pgHelper.getAllDonations();
        });
        //verify that dbConnection gets closed
        verify(dbConnection, times(1)).close();
    }


}