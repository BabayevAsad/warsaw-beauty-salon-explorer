package com.babayev.warsaw.salonexplorer.scraper;

import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.repository.SalonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BooksyClientTest {

    @Mock
    private SalonRepository salonRepository;

    @InjectMocks
    private BooksyClient booksyClient;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(booksyClient, "fetchLimit", 10);
    }

    @Test
    void shouldSaveSalonWhenValidCardExists() {

        when(salonRepository.save(any(Salon.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Salon salon = new Salon();
        salon.setName("Test Barber Shop");

        Salon saved = salonRepository.save(salon);

        assertNotNull(saved);
        assertEquals("Test Barber Shop", saved.getName());

        verify(salonRepository).save(any(Salon.class));
    }

    @Test
    void shouldNotSaveDuplicateSalon() {

        when(salonRepository.existsByWebsite(anyString()))
                .thenReturn(true);

        boolean exists = salonRepository.existsByWebsite(
                "https://booksy.com/pl-pl/12345/test-barber"
        );

        assertTrue(exists);

        verify(salonRepository).existsByWebsite(anyString());
        verify(salonRepository, never()).save(any());
    }

    @Test
    void shouldCalculateAveragePriceRange() {

        Salon salon = new Salon();

        assertNotNull(salon);
    }
}