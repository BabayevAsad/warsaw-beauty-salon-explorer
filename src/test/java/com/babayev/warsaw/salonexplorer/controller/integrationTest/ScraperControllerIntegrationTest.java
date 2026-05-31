package com.babayev.warsaw.salonexplorer.controller.integrationTest;

import com.babayev.warsaw.salonexplorer.WarsawBeautySalonExplorerApplication;
import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.repository.SalonRepository;
import com.babayev.warsaw.salonexplorer.scraper.BooksyClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {WarsawBeautySalonExplorerApplication.class, ScraperControllerIntegrationTest.TestConfig.class})
@AutoConfigureMockMvc
@Transactional
class ScraperControllerIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public BooksyClient booksyClient() {
            return Mockito.mock(BooksyClient.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BooksyClient booksyClient;

    @Autowired
    private SalonRepository salonRepository;

    @Test
    void shouldTriggerScrapeSuccessfully() throws Exception {
        Salon salon = new Salon();
        salon.setName("Integration Scrape");

        when(booksyClient.fetchAndParseSalons()).thenReturn(List.of(salon));

        mockMvc.perform(post("/api/scraper/trigger")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Integration Scrape"));

        assert(salonRepository.count() == 1);
    }
}