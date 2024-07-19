package com.demo;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.database.DatabaseService;
import com.demo.database.PostgresHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * @Author Gabrielle Olivera
 * This class tests the web service API endpoints.
 */

@WebMvcTest(DonationWebServiceController.class)
public class DontationWebServiceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseService databaseService;

    @MockBean
    private PostgresHelper pgHelper;


    @Test
    void getAllDonationsShouldReturnListOfDonations() throws Exception {
        ArrayList<Donation> donations = new ArrayList<Donation>();
        donations.add(new Donation("Joe", "Black", "$50", "money", Date.valueOf("2024-01-01")));
        /*
        TODO: Commenting out until I have more time to get this to work.
        ObjectMapper objectMapper = new ObjectMapper();
        String donationsJSON = objectMapper.writeValueAsString(donations);
        */

        when(pgHelper.getAllDonations()).thenReturn(donations);
        this.mockMvc.perform(get("/donations"))
            .andDo(print())
            .andExpect(status().isOk());

    }

    
    
}
