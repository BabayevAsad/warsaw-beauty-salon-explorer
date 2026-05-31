package com.babayev.warsaw.salonexplorer.service;

import com.babayev.warsaw.salonexplorer.dto.TreatmentDTO;

import java.util.List;

public interface TreatmentService {

    List<TreatmentDTO> findBySalonId(Long salonId);
    TreatmentDTO saveTreatment(TreatmentDTO treatmentDTO, Long salonId);
    TreatmentDTO updateTreatment(TreatmentDTO treatmentDTO, Long treatmentId);
    void deleteTreatment(Long treatmentId);
}