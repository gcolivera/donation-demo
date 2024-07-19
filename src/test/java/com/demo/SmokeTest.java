package com.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * @Author Gabrielle Olivera
 * This is another sanity check to just make sure the controllers can be loaded.
 * This app has two controllers. One for the webservice API layer and another for the Donation Form page.
 */

@SpringBootTest
public class SmokeTest {
    @Autowired
    private DonationWebServiceController webServiceController;

    @Autowired
    private DonationFormController formController;

    @Test
    void webServiceContextLoads() throws Exception {
        assertThat(webServiceController).isNotNull();
    }

    @Test
    void donationFormContextLoads() throws Exception {
        assertThat(formController).isNotNull();
    }
}
