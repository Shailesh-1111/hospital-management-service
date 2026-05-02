package com.hospital.backend.controller;

import com.hospital.backend.dto.BedResponse;
import com.hospital.backend.dto.BookingRequest;
import com.hospital.backend.dto.HistoryResponse;
import com.hospital.backend.enums.BedType;
import com.hospital.backend.service.BedService;
import com.hospital.backend.service.PricingService;
import com.hospital.backend.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beds")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BedController {

    private final BedService bedService;
    private final PricingService pricingService;
    private final SseService sseService;

    @GetMapping
    public ResponseEntity<List<BedResponse>> getBeds(
            @RequestParam Integer floor,
            @RequestParam(required = false) String lastId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(bedService.getBedsByFloor(floor, lastId, size));
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoryResponse>> getHistory() {
        return ResponseEntity.ok(bedService.getHistory());
    }

    @GetMapping("/pricing")
    public ResponseEntity<Double> getPricing(
            @RequestParam BedType type,
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(pricingService.calculatePrice(type, LocalDateTime.parse(from), LocalDateTime.parse(to)));
    }

    @PostMapping("/{bedId}/book")
    public ResponseEntity<BedResponse> bookBed(
            @PathVariable String bedId,
            @RequestBody BookingRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return ResponseEntity.ok(bedService.bookBed(bedId, request, idempotencyKey));
    }

    @PostMapping("/{bedId}/discharge")
    public ResponseEntity<BedResponse> dischargePatient(@PathVariable String bedId) {
        return ResponseEntity.ok(bedService.dischargePatient(bedId));
    }

    @GetMapping("/stream")
    public SseEmitter streamUpdates() {
        return sseService.createEmitter();
    }
}
