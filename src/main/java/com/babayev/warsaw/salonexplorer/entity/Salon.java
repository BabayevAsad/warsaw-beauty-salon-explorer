package com.babayev.warsaw.salonexplorer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salon",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private String website;
    private double rating;
    private int reviewsCount;
    private String priceRange;

    @OneToMany( mappedBy = "salon", cascade = CascadeType.ALL)
    private List<Treatment > treatments = new ArrayList<>();
}