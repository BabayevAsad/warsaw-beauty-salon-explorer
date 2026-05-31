package com.babayev.warsaw.salonexplorer.service.impl;

import com.babayev.warsaw.salonexplorer.dto.TreatmentDTO;
import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.entity.Treatment;
import com.babayev.warsaw.salonexplorer.repository.SalonRepository;
import com.babayev.warsaw.salonexplorer.repository.TreatmentRepository;
import com.babayev.warsaw.salonexplorer.service.TreatmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final SalonRepository salonRepository;

    @Override
    public List<TreatmentDTO> findBySalonId(Long salonId) {

        List<Treatment> treatments = treatmentRepository.findBySalonId(salonId);

        return treatments.stream().map(t->TreatmentDTO.builder()
                .id(t.getId())
                .name(t.getName())
                .price(t.getPrice())
                .build()).toList();
    }

    @Override
    public TreatmentDTO saveTreatment(TreatmentDTO dto, Long salonId) {
        Salon salon = salonRepository.findById(salonId)
                .orElseThrow(() -> new RuntimeException("Salon not found"));

        Treatment t = new Treatment();
        t.setName(dto.getName());
        t.setPrice(dto.getPrice());
        t.setSalon(salon);

        Treatment saved = treatmentRepository.save(t);

        return TreatmentDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .price(saved.getPrice())
                .build();
    }

    @Override
    public TreatmentDTO updateTreatment(TreatmentDTO dto, Long treatmentId) {

        Treatment t = treatmentRepository.findById(treatmentId).orElseThrow(() -> new EntityNotFoundException("treatment id not found"));

        t.setName(dto.getName());
        t.setPrice(dto.getPrice());

        treatmentRepository.save(t);

        return TreatmentDTO.builder()
                .id(t.getId())
                .name(t.getName())
                .price(t.getPrice())
                .build();
    }

    @Override
    public void deleteTreatment(Long treatmentId) {
        treatmentRepository.deleteById(treatmentId);
    }
}