package com.hospital.backend.entity;

import com.hospital.backend.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bed_id", nullable = false)
    private Bed bed;

    private String patientName;
    private String mobile;
    private Integer age;

    private LocalDateTime fromTime;
    private LocalDateTime expectedToTime;
    private LocalDateTime actualDischargeTime;

    private Double totalCost;

    @Column(unique = true)
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
