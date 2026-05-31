package com.babayev.warsaw.salonexplorer.repository;

import com.babayev.warsaw.salonexplorer.WarsawBeautySalonExplorerApplication;
import com.babayev.warsaw.salonexplorer.entity.Salon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = WarsawBeautySalonExplorerApplication.class)
@ActiveProfiles("test")
class SalonRepositoryTest {

    @Autowired
    private SalonRepository salonRepository;

    @BeforeEach
    void setUp() {
        salonRepository.deleteAll();

        Salon salon1 = new Salon();
        salon1.setName("Luxury Beauty");
        salon1.setAddress("Marszałkowska, Warsaw");
        salon1.setWebsite("www.luxurybeauty.pl");

        Salon salon2 = new Salon();
        salon2.setName("Urban Style");
        salon2.setAddress("Nowy Świat, Warsaw");
        salon2.setWebsite("www.urbanstyle.pl");

        salonRepository.saveAll(List.of(salon1, salon2));
    }

    @Test
    void findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase_ShouldReturnMatchingSalons() {
        List<Salon> found = salonRepository.findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase("luxury", "marszałkowska");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Luxury Beauty");
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnMatchingSalons() {
        List<Salon> found = salonRepository.findByNameContainingIgnoreCase("urban");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Urban Style");
    }

    @Test
    void findByAddressContainingIgnoreCase_ShouldReturnMatchingSalons() {
        List<Salon> found = salonRepository.findByAddressContainingIgnoreCase("warsaw");

        assertThat(found).hasSize(2);
    }

    @Test
    void existsByWebsite_ShouldReturnTrueIfExists() {
        boolean exists = salonRepository.existsByWebsite("www.luxurybeauty.pl");
        assertThat(exists).isTrue();
    }

    @Test
    void existsByWebsite_ShouldReturnFalseIfNotExists() {
        boolean exists = salonRepository.existsByWebsite("www.nonexistent.pl");
        assertThat(exists).isFalse();
    }
}