package com.babayev.warsaw.salonexplorer.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateSalonDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @Pattern(
            regexp = "^[0-9+\\-() ]{7,20}$",
            message = "Invalid phone number")
    private String phone;

    @Size(max = 200)
    private String website;

    @Min(value = 0)
    @Max(value = 5)
    private Double rating;

    @Min(value = 0)
    private Integer reviewsCount;

    @NotBlank(message = "Price range is required")
    private String priceRange;
}