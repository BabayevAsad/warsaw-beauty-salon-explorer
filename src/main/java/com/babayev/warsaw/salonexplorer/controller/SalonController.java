package com.babayev.warsaw.salonexplorer.controller;

import com.babayev.warsaw.salonexplorer.dto.SalonDetailsDTO;
import com.babayev.warsaw.salonexplorer.dto.SalonListDTO;
import com.babayev.warsaw.salonexplorer.dto.UpdateSalonDTO;
import com.babayev.warsaw.salonexplorer.service.SalonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/rest/api/salons")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;

    @GetMapping
    ResponseEntity<Page<SalonListDTO>> getAllSalons(@PageableDefault(size = 12, sort = "name") Pageable pageable){

        return ResponseEntity.ok(salonService.getAllSalons(pageable));
    }

    @GetMapping("/{id}")
    ResponseEntity<SalonDetailsDTO> getSalonById(@PathVariable Long id){
        return ResponseEntity.ok(salonService.getSalonById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SalonListDTO>> search(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String address){

        return ResponseEntity.ok(salonService.search(name, address));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SalonDetailsDTO> updateSalon(@Valid @RequestBody UpdateSalonDTO updateSalonDTO, @PathVariable Long id){
        SalonDetailsDTO result =salonService.updateSalon(updateSalonDTO, id);
        return ResponseEntity.ok(result);
    }
}
