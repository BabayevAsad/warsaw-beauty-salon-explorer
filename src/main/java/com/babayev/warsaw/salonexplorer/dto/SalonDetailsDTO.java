package com.babayev.warsaw.salonexplorer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalonDetailsDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String website;
    private Double rating;
    private Integer reviewsCount;
    private String priceRange;
    private List<TreatmentDTO> treatments;
}