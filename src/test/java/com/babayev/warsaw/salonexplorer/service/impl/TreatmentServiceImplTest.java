package com.babayev.warsaw.salonexplorer.service.impl;

import com.babayev.warsaw.salonexplorer.dto.TreatmentDTO;
import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.entity.Treatment;
import com.babayev.warsaw.salonexplorer.repository.SalonRepository;
import com.babayev.warsaw.salonexplorer.repository.TreatmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentServiceImplTest {

    @Mock private TreatmentRepository treatmentRepository;
    @Mock private SalonRepository salonRepository;
    @InjectMocks private TreatmentServiceImpl treatmentService;

    @Test
    void findBySalonId_ReturnsMappedList() {
        Treatment t = new Treatment();
        t.setId(1L);
        t.setName("Haircut");
        t.setPrice(50.0);

        when(treatmentRepository.findBySalonId(1L)).thenReturn(List.of(t));

        List<TreatmentDTO> result = treatmentService.findBySalonId(1L);

        assertEquals(1, result.size());
        assertEquals("Haircut", result.get(0).getName());
    }

    @Test
    void saveTreatment_LinksToSalonAndReturnsDto() {
        Salon salon = new Salon();
        salon.setId(1L);
        TreatmentDTO dto = TreatmentDTO.builder().name("Manicure").price(100.0).build();

        when(salonRepository.findById(1L)).thenReturn(Optional.of(salon));
        when(treatmentRepository.save(any(Treatment.class))).thenAnswer(i -> {
            Treatment t = i.getArgument(0);
            t.setId(99L);
            return t;
        });

        TreatmentDTO result = treatmentService.saveTreatment(dto, 1L);

        assertEquals(99L, result.getId());
        assertEquals("Manicure", result.getName());
        verify(treatmentRepository).save(any(Treatment.class));
    }

    @Test
    void updateTreatment_UpdatesFieldsCorrectly() {
        Treatment existing = new Treatment();
        existing.setId(1L);
        TreatmentDTO updateDto = TreatmentDTO.builder().name("Spa").price(200.0).build();

        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(existing));

        TreatmentDTO result = treatmentService.updateTreatment(updateDto, 1L);

        assertEquals("Spa", result.getName());
        verify(treatmentRepository).save(existing);
    }
}