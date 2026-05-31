package com.babayev.warsaw.salonexplorer.repository;

import com.babayev.warsaw.salonexplorer.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalonRepository extends JpaRepository<Salon,Long> {
    List<Salon> findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(String name, String address);
    List<Salon> findByNameContainingIgnoreCase(String name);
    List<Salon> findByAddressContainingIgnoreCase( String address);
    boolean existsByWebsite(String website);
}