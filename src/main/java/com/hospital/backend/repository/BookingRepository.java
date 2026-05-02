package com.hospital.backend.repository;

import com.hospital.backend.entity.Booking;
import com.hospital.backend.entity.Bed;
import com.hospital.backend.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByIdempotencyKey(String idempotencyKey);
    Optional<Booking> findByBedAndStatus(Bed bed, BookingStatus status);
    List<Booking> findByStatusOrderByActualDischargeTimeDesc(BookingStatus status);
}
