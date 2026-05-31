package com.babayev.warsaw.salonexplorer.controller;

import com.babayev.warsaw.salonexplorer.dto.SalonDetailsDTO;
import com.babayev.warsaw.salonexplorer.dto.SalonListDTO;
import com.babayev.warsaw.salonexplorer.dto.UpdateSalonDTO;
import com.babayev.warsaw.salonexplorer.service.SalonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SalonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SalonService salonService;

    @InjectMocks
    private SalonController salonController;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "/rest/api/salons";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(salonController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void shouldReturnAllSalonsPaged() throws Exception {
        SalonListDTO salonDto = new SalonListDTO();
        salonDto.setName("Luxury Beauty");

        List<SalonListDTO> content = List.of(salonDto);
        Page<SalonListDTO> page = new PageImpl<>(content, Pageable.ofSize(12), 1);

        when(salonService.getAllSalons(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Luxury Beauty"));
    }

    @Test
    void shouldReturnSalonById() throws Exception {
        SalonDetailsDTO detailsDto = new SalonDetailsDTO();
        detailsDto.setName("Luxury Beauty");

        when(salonService.getSalonById(1L)).thenReturn(detailsDto);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luxury Beauty"));
    }

    @Test
    void shouldSearchSalons() throws Exception {
        SalonListDTO salonDto = new SalonListDTO();
        salonDto.setName("Urban Style");

        when(salonService.search("urban", null)).thenReturn(List.of(salonDto));

        mockMvc.perform(get(BASE_URL + "/search")
                        .param("name", "urban"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Urban Style"));
    }

    @Test
    void shouldUpdateSalonSuccessfully() throws Exception {
        UpdateSalonDTO updateDto = new UpdateSalonDTO();
        updateDto.setName("New Name");
        updateDto.setAddress("New Address");
        updateDto.setPriceRange("$$");

        SalonDetailsDTO resultDto = new SalonDetailsDTO();
        resultDto.setName("New Name");

        when(salonService.updateSalon(any(UpdateSalonDTO.class), eq(1L))).thenReturn(resultDto);

        mockMvc.perform(put(BASE_URL + "/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }
}