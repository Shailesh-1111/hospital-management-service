package com.hospital.backend.dto;

import com.hospital.backend.entity.Bed;
import com.hospital.backend.entity.Booking;
import lombok.Data;

@Data
public class BedResponse {
    private String id;
    private Integer floor;
    private String type;
    private String status;
    private String patientName;
    private String mobile;
    private Integer age;
    private String condition;
    private String occupiedFrom;
    private String expectedDischarge;
    private Double totalCost;

    public static BedResponse from(Bed bed, Booking activeBooking) {
        BedResponse res = new BedResponse();
        res.setId(bed.getId());
        res.setFloor(bed.getFloorNumber());
        res.setType(bed.getType().name());
        res.setStatus(bed.getStatus().name());
        
        if (activeBooking != null) {
            res.setPatientName(activeBooking.getPatientName());
            res.setMobile(activeBooking.getMobile());
            res.setAge(activeBooking.getAge());
            res.setCondition("General Admission");
            res.setOccupiedFrom(activeBooking.getFromTime() != null ? activeBooking.getFromTime().toString() : null);
            res.setExpectedDischarge(activeBooking.getExpectedToTime() != null ? activeBooking.getExpectedToTime().toString() : null);
            res.setTotalCost(activeBooking.getTotalCost());
        }
        return res;
    }
}
