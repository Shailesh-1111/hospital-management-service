package com.hospital.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingRequest {
    private String patientName;
    private String mobile;
    private Integer age;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
}
