package com.hospital.backend.dto;

import com.hospital.backend.entity.Booking;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class HistoryResponse {
    private Long id;
    private String patientName;
    private String bedType;
    private String bedId;
    private String mobile;
    private Integer age;
    private Double totalCost;
    private String fromTime;
    private String expectedToTime;
    private String date;

    public static HistoryResponse from(Booking booking) {
        HistoryResponse res = new HistoryResponse();
        res.setId(booking.getId());
        res.setPatientName(booking.getPatientName());
        res.setBedType(booking.getBed().getType().name());
        res.setBedId(booking.getBed().getId());
        res.setMobile(booking.getMobile());
        res.setAge(booking.getAge());
        res.setTotalCost(booking.getTotalCost());
        
        if (booking.getFromTime() != null) {
            res.setFromTime(booking.getFromTime().toString());
        }
        if (booking.getExpectedToTime() != null) {
            res.setExpectedToTime(booking.getExpectedToTime().toString());
        }
        if (booking.getActualDischargeTime() != null) {
            res.setDate(booking.getActualDischargeTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else {
            res.setDate("N/A");
        }
        return res;
    }
}
