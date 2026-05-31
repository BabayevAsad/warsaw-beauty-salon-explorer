package com.babayev.warsaw.salonexplorer.service.impl;

import com.babayev.warsaw.salonexplorer.dto.SalonDetailsDTO;
import com.babayev.warsaw.salonexplorer.dto.SalonListDTO;
import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.repository.SalonRepository;
import com.babayev.warsaw.salonexplorer.service.TreatmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalonServiceImplTest {

    @Mock private SalonRepository salonRepository;
    @Mock private TreatmentService treatmentService;
    @InjectMocks private SalonServiceImpl salonService;

    @Test
    void getSalonById_Success() {
        Salon mockSalon = new Salon();
        mockSalon.setId(1L);
        mockSalon.setName("Test Salon");

        when(salonRepository.findById(1L)).thenReturn(Optional.of(mockSalon));

        SalonDetailsDTO result = salonService.getSalonById(1L);

        assertNotNull(result);
        assertEquals("Test Salon", result.getName());
        verify(treatmentService, times(1)).findBySalonId(1L);
    }

    @Test
    void getSalonById_NotFound_ThrowsException() {
        when(salonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> salonService.getSalonById(1L));
    }

    @Test
    void search_ByName_ReturnsList() {
        Salon salon = new Salon();
        salon.setName("Style Salon");
        when(salonRepository.findByNameContainingIgnoreCase("Style")).thenReturn(List.of(salon));

        List<SalonListDTO> results = salonService.search("Style", null);

        assertEquals(1, results.size());
        assertEquals("Style Salon", results.get(0).getName());
    }

    @Test
    void getAllSalons_ReturnsMappedPage() {
        Page<Salon> page = new PageImpl<>(List.of(new Salon()));
        when(salonRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<SalonListDTO> result = salonService.getAllSalons(PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        verify(salonRepository).findAll(any(PageRequest.class));
    }
}