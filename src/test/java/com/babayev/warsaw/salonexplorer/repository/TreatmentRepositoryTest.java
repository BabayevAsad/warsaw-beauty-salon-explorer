package com.babayev.warsaw.salonexplorer.repository;

import com.babayev.warsaw.salonexplorer.WarsawBeautySalonExplorerApplication;
import com.babayev.warsaw.salonexplorer.entity.Salon;
import com.babayev.warsaw.salonexplorer.entity.Treatment;
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
class TreatmentRepositoryTest {

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private SalonRepository salonRepository;

    private Salon savedSalon;

    @BeforeEach
    void setUp() {
        treatmentRepository.deleteAll();
        salonRepository.deleteAll();

        Salon salon = new Salon();
        salon.setName("Luxury Beauty");
        savedSalon = salonRepository.save(salon);

        Treatment t1 = new Treatment();
        t1.setName("Manicure");
        t1.setSalon(savedSalon);

        Treatment t2 = new Treatment();
        t2.setName("Pedicure");
        t2.setSalon(savedSalon);

        treatmentRepository.saveAll(List.of(t1, t2));
    }

    @Test
    void findBySalonId_ShouldReturnTreatmentsForGivenSalon() {
        List<Treatment> found = treatmentRepository.findBySalonId(savedSalon.getId());

        assertThat(found).hasSize(2);
        assertThat(found).extracting(Treatment::getName)
                .containsExactlyInAnyOrder("Manicure", "Pedicure");
    }
}