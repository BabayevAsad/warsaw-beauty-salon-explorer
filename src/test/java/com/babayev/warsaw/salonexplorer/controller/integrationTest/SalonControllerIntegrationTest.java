package com.babayev.warsaw.salonexplorer.controller.integrationTest;

import com.babayev.warsaw.salonexplorer.WarsawBeautySalonExplorerApplication;
import com.babayev.warsaw.salonexplorer.dto.UpdateSalonDTO;
import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.repository.SalonRepository;
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
class SalonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private ObjectMapper mapper;

    private static final String BASE_URL = "/rest/api/salons";
    private Long existingSalonId;

    @BeforeEach
    void setUp() {
        salonRepository.deleteAll();

        Salon salon = new Salon();
        salon.setName("Luxury Beauty");
        salon.setAddress("Marszałkowska, Warsaw");
        salon.setWebsite("www.luxurybeauty.pl");
        salon.setPriceRange("$$$");

        existingSalonId = salonRepository.save(salon).getId();
    }

    @Test
    void shouldReturnAllSalonsPaged() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Luxury Beauty"));
    }

    @Test
    void shouldReturnSalonById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + existingSalonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luxury Beauty"));
    }

    @Test
    void shouldSearchSalons() throws Exception {
        mockMvc.perform(get(BASE_URL + "/search")
                        .param("name", "Luxury"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Luxury Beauty"));
    }

    @Test
    void shouldUpdateExistingSalon() throws Exception {
        UpdateSalonDTO updateDto = new UpdateSalonDTO();
        updateDto.setName("Updated Luxury Beauty");
        updateDto.setAddress("New Address, Warsaw");
        updateDto.setPriceRange("$$");

        updateDto.setRating(4.5);
        updateDto.setReviewsCount(10);

        mockMvc.perform(put(BASE_URL + "/update/" + existingSalonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenSalonNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999"))
                .andExpect(status().isNotFound());
    }
}