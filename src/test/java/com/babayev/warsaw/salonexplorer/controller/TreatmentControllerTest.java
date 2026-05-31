package com.babayev.warsaw.salonexplorer.controller;

import com.babayev.warsaw.salonexplorer.dto.TreatmentDTO;
import com.babayev.warsaw.salonexplorer.service.TreatmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TreatmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TreatmentService treatmentService;

    @InjectMocks
    private TreatmentController treatmentController;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "/rest/api/treatments";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(treatmentController).build();
    }

    @Test
    void shouldSaveTreatmentSuccessfully() throws Exception {
        TreatmentDTO dto = new TreatmentDTO();
        dto.setName("Manicure");
        dto.setPrice(50.0);

        when(treatmentService.saveTreatment(any(TreatmentDTO.class), eq(1L))).thenReturn(dto);

        mockMvc.perform(post(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Manicure"))
                .andExpect(jsonPath("$.price").value(50.0));
    }

    @Test
    void shouldUpdateTreatmentSuccessfully() throws Exception {
        TreatmentDTO dto = new TreatmentDTO();
        dto.setName("Pedicure");
        dto.setPrice(75.0);

        when(treatmentService.updateTreatment(any(TreatmentDTO.class), eq(1L))).thenReturn(dto);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pedicure"))
                .andExpect(jsonPath("$.price").value(75.0));
    }

    @Test
    void shouldDeleteTreatmentSuccessfully() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(treatmentService).deleteTreatment(1L);
    }
}