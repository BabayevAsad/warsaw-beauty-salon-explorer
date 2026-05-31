package com.babayev.warsaw.salonexplorer.controller;

import com.babayev.warsaw.salonexplorer.dto.TreatmentDTO;
import com.babayev.warsaw.salonexplorer.service.TreatmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/api/treatments")
@RequiredArgsConstructor
public class TreatmentController {

    private final TreatmentService treatmentService;

    @PostMapping("/{salonId}")
    public ResponseEntity<TreatmentDTO> saveTreatment(@Valid @RequestBody TreatmentDTO dto,
                                                      @PathVariable Long salonId) {

        return ResponseEntity.ok(treatmentService.saveTreatment(dto, salonId));
    }

    @PutMapping("/{treatmentId}")
    public ResponseEntity<TreatmentDTO> updateTreatment(@Valid @RequestBody TreatmentDTO dto,
                                                        @PathVariable Long treatmentId) {

        return ResponseEntity.ok(treatmentService.updateTreatment(dto, treatmentId));
    }

    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Long treatmentId) {

        treatmentService.deleteTreatment(treatmentId);
        return ResponseEntity.noContent().build();
    }
}