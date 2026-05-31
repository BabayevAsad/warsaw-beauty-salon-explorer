package com.babayev.warsaw.salonexplorer.controller;

import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.repository.SalonRepository;
import com.babayev.warsaw.salonexplorer.scraper.BooksyClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scraper")
@AllArgsConstructor
public class ScraperController {

    private final BooksyClient booksyClient;
    private final SalonRepository salonRepository;

    @PostMapping("/trigger")
    public ResponseEntity<List<Salon>> triggerScrape() {
        List<Salon> savedSalons = booksyClient.fetchAndParseSalons();
        salonRepository.saveAll(savedSalons);
        return ResponseEntity.ok(savedSalons);
    }

    @GetMapping("/test-mapping")
    public List<Salon> testMapping() {
        return booksyClient.fetchAndParseSalons();
    }
}
