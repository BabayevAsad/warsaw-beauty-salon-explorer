package com.babayev.warsaw.salonexplorer.controller;

import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.repository.SalonRepository;
import com.babayev.warsaw.salonexplorer.scraper.BooksyClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScraperControllerTest {

    @Mock
    private BooksyClient booksyClient;

    @Mock
    private SalonRepository salonRepository;

    @InjectMocks
    private ScraperController scraperController;

    @Test
    void shouldTriggerScrapeAndSave() {
        Salon salon = new Salon();
        salon.setName("Scraped Salon");
        List<Salon> mockSalons = List.of(salon);

        when(booksyClient.fetchAndParseSalons()).thenReturn(mockSalons);

        ResponseEntity<List<Salon>> response = scraperController.triggerScrape();

        assertEquals(200, response.getStatusCode().value());
        verify(salonRepository, times(1)).saveAll(mockSalons);
    }
}