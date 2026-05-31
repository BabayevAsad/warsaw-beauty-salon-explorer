package com.babayev.warsaw.salonexplorer.service.impl;

import com.babayev.warsaw.salonexplorer.dto.SalonDetailsDTO;
import com.babayev.warsaw.salonexplorer.dto.SalonListDTO;
import com.babayev.warsaw.salonexplorer.dto.UpdateSalonDTO;
import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.repository.SalonRepository;
import com.babayev.warsaw.salonexplorer.service.SalonService;
import com.babayev.warsaw.salonexplorer.service.TreatmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;
    private final TreatmentService treatmentService;

    @Override
    public Page<SalonListDTO> getAllSalons(Pageable pageable){

        return salonRepository.findAll(pageable)
                .map(this::toSalonListDto);
    }

    @Override
    public SalonDetailsDTO getSalonById(Long id){

        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Salon not found with id: " + id));

        return toSalonDetailsDto(salon);
    }

    @Override
    public List<SalonListDTO> search(String name, String address) {
        List<Salon> salon=null;

        if (name != null && address != null) {
            salon = salonRepository.findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(name, address);
        } else if (name != null) {
            salon = salonRepository.findByNameContainingIgnoreCase(name);
        } else if (address != null) {
            salon = salonRepository.findByAddressContainingIgnoreCase(address);
        }

        if (salon == null) {
            return new ArrayList<>();
        }

        return salon.stream()
                .map(this::toSalonListDto)
                .toList();
    }

    @Override
    public SalonDetailsDTO updateSalon(UpdateSalonDTO updateSalonDTO, Long id) {
        Salon salon = salonRepository.findById(id).orElseThrow(() -> new RuntimeException("Salon not found"));

        salon.setName(updateSalonDTO.getName());
        salon.setAddress(updateSalonDTO.getAddress());
        salon.setPhone(updateSalonDTO.getPhone());
        salon.setWebsite(updateSalonDTO.getWebsite());
        salon.setRating(updateSalonDTO.getRating());
        salon.setReviewsCount(updateSalonDTO.getReviewsCount());
        salon.setPriceRange(updateSalonDTO.getPriceRange());

        salonRepository.save(salon);

        return toSalonDetailsDto(salon);
    }

    private SalonListDTO toSalonListDto(Salon s){
        return SalonListDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .address(s.getAddress())
                .rating(s.getRating())
                .priceRange(s.getPriceRange())
                .build();
    }

    private SalonDetailsDTO toSalonDetailsDto(Salon s){
        return SalonDetailsDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .address(s.getAddress())
                .phone(s.getPhone())
                .website(s.getWebsite())
                .rating(s.getRating())
                .reviewsCount(s.getReviewsCount())
                .priceRange(s.getPriceRange())
                .treatments(treatmentService.findBySalonId(s.getId()))
                .build();
    }
}