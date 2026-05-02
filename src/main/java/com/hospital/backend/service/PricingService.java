package com.hospital.backend.service;
import com.hospital.backend.enums.BedType;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class PricingService {
    public Double calculatePrice(BedType type, LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) return 0.0;
        long days = ChronoUnit.DAYS.between(from, to);
        if (days == 0) days = 1;
        double rate = (type == BedType.ICU) ? 5000.0 : 2000.0;
        return days * rate;
    }
}
