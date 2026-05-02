package com.hospital.backend.component;

import com.hospital.backend.entity.Bed;
import com.hospital.backend.enums.BedStatus;
import com.hospital.backend.enums.BedType;
import com.hospital.backend.repository.BedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final BedRepository bedRepository;
    private final com.hospital.backend.repository.BookingRepository bookingRepository;

    @Override
    public void run(String... args) throws Exception {
        if (bedRepository.count() <= 60) {
            bookingRepository.deleteAll();
            bedRepository.deleteAll();

            List<Bed> beds = new ArrayList<>();
            for (int i=1; i<=75; i++) {
                Bed b = new Bed();
                b.setId("1" + String.format("%03d", i));
                b.setFloorNumber(1);
                b.setType(i <= 20 ? BedType.ICU : BedType.NON_ICU);
                b.setStatus(BedStatus.AVAILABLE);
                beds.add(b);
            }
            for (int i=1; i<=75; i++) {
                Bed b = new Bed();
                b.setId("2" + String.format("%03d", i));
                b.setFloorNumber(2);
                b.setType(i <= 30 ? BedType.ICU : BedType.NON_ICU);
                b.setStatus(BedStatus.AVAILABLE);
                beds.add(b);
            }
            for (int i=1; i<=75; i++) {
                Bed b = new Bed();
                b.setId("3" + String.format("%03d", i));
                b.setFloorNumber(3);
                b.setType(BedType.NON_ICU);
                b.setStatus(BedStatus.AVAILABLE);
                beds.add(b);
            }
            bedRepository.saveAll(beds);
            System.out.println("Seeded exactly 225 beds into the Database!");
        }
    }
}
