package com.babayev.warsaw.salonexplorer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalonListDTO {
    private Long id;
    private String name;
    private String address;
    private Double rating;
    private String priceRange;
}