package com.hospital.backend.service;

import com.hospital.backend.dto.BedResponse;
import com.hospital.backend.dto.BookingRequest;
import com.hospital.backend.dto.HistoryResponse;
import com.hospital.backend.entity.Bed;
import com.hospital.backend.entity.Booking;
import com.hospital.backend.enums.BedStatus;
import com.hospital.backend.enums.BookingStatus;
import com.hospital.backend.repository.BedRepository;
import com.hospital.backend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BedService {
    private final BedRepository bedRepository;
    private final BookingRepository bookingRepository;
    private final PricingService pricingService;
    private final SseService sseService;

    public List<BedResponse> getBedsByFloor(Integer floor, String lastId, int size) {
        Pageable pageable = PageRequest.of(0, size);
        List<Bed> beds;
        if (lastId == null || lastId.isEmpty()) {
            beds = bedRepository.findByFloorNumberOrderByIdAsc(floor, pageable).getContent();
        } else {
            beds = bedRepository.findByFloorNumberAndIdGreaterThanOrderByIdAsc(floor, lastId, pageable);
        }
        
        return beds.stream().map(bed -> {
            Booking activeBooking = null;
            if (bed.getStatus() == BedStatus.OCCUPIED) {
                activeBooking = bookingRepository.findByBedAndStatus(bed, BookingStatus.ACTIVE).orElse(null);
            }
            return BedResponse.from(bed, activeBooking);
        }).collect(Collectors.toList());
    }

    public List<HistoryResponse> getHistory() {
        return bookingRepository.findByStatusOrderByActualDischargeTimeDesc(BookingStatus.DISCHARGED)
                .stream().map(HistoryResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public BedResponse bookBed(String bedId, BookingRequest request, String idempotencyKey) {
        Optional<Booking> existing = bookingRepository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            return BedResponse.from(existing.get().getBed(), existing.get());
        }

        Bed bed = bedRepository.findByIdWithLock(bedId).orElseThrow(() -> new RuntimeException("Bed not found"));
        if (bed.getStatus() != BedStatus.AVAILABLE) {
            throw new RuntimeException("Bed already occupied");
        }

        bed.setStatus(BedStatus.OCCUPIED);
        bedRepository.save(bed);

        Booking booking = new Booking();
        booking.setBed(bed);
        booking.setPatientName(request.getPatientName());
        booking.setMobile(request.getMobile());
        booking.setAge(request.getAge());
        booking.setFromTime(request.getFromTime());
        booking.setExpectedToTime(request.getToTime());
        booking.setIdempotencyKey(idempotencyKey);
        booking.setStatus(BookingStatus.ACTIVE);
        
        Double cost = pricingService.calculatePrice(bed.getType(), request.getFromTime(), request.getToTime());
        booking.setTotalCost(cost);
        
        bookingRepository.save(booking);

        BedResponse response = BedResponse.from(bed, booking);
        sseService.sendUpdate(response);
        return response;
    }

    @Transactional
    public BedResponse dischargePatient(String bedId) {
        Bed bed = bedRepository.findById(bedId).orElseThrow(() -> new RuntimeException("Bed not found"));
        bed.setStatus(BedStatus.AVAILABLE);
        bedRepository.save(bed);

        Optional<Booking> activeBooking = bookingRepository.findByBedAndStatus(bed, BookingStatus.ACTIVE);
        if (activeBooking.isPresent()) {
            Booking booking = activeBooking.get();
            booking.setStatus(BookingStatus.DISCHARGED);
            booking.setActualDischargeTime(LocalDateTime.now());
            bookingRepository.save(booking);
        }

        BedResponse response = BedResponse.from(bed, null);
        sseService.sendUpdate(response);
        return response;
    }
}
