package com.babayev.warsaw.salonexplorer.controller.integrationTest;

import com.babayev.warsaw.salonexplorer.WarsawBeautySalonExplorerApplication;
import com.babayev.warsaw.salonexplorer.dto.TreatmentDTO;
import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.entity.Treatment;
import com.babayev.warsaw.salonexplorer.repository.SalonRepository;
import com.babayev.warsaw.salonexplorer.repository.TreatmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = WarsawBeautySalonExplorerApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TreatmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private ObjectMapper mapper;

    private static final String BASE_URL = "/rest/api/treatments";
    private Long salonId;
    private Long treatmentId;

    @BeforeEach
    void setUp() {
        treatmentRepository.deleteAll();
        salonRepository.deleteAll();

        Salon salon = new Salon();
        salon.setName("Luxury Beauty");
        salonId = salonRepository.save(salon).getId();

        Treatment treatment = new Treatment();
        treatment.setName("Manicure");
        treatment.setPrice(100.0);
        treatment.setSalon(salon);
        treatmentId = treatmentRepository.save(treatment).getId();
    }

    @Test
    void shouldSaveTreatment() throws Exception {
        TreatmentDTO dto = new TreatmentDTO();
        dto.setName("Pedicure");
        dto.setPrice(150.0);

        mockMvc.perform(post(BASE_URL + "/" + salonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pedicure"));
    }

    @Test
    void shouldUpdateTreatment() throws Exception {
        TreatmentDTO dto = new TreatmentDTO();
        dto.setName("Updated Manicure");
        dto.setPrice(120.0);

        mockMvc.perform(put(BASE_URL + "/" + treatmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Manicure"));
    }

    @Test
    void shouldDeleteTreatment() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + treatmentId))
                .andExpect(status().isNoContent());
    }
}