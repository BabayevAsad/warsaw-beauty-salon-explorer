package com.babayev.warsaw.salonexplorer.service;

import com.babayev.warsaw.salonexplorer.dto.SalonDetailsDTO;
import com.babayev.warsaw.salonexplorer.dto.SalonListDTO;
import com.babayev.warsaw.salonexplorer.dto.UpdateSalonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SalonService {

    Page<SalonListDTO> getAllSalons(Pageable pageable);
    SalonDetailsDTO getSalonById(Long id);
    List<SalonListDTO> search(String name, String address);
    SalonDetailsDTO updateSalon(UpdateSalonDTO updateSalonDTO, Long id);
}