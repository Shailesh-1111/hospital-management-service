package com.hospital.backend.entity;

import com.hospital.backend.enums.BedStatus;
import com.hospital.backend.enums.BedType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "beds")
@Data
public class Bed {
    @Id
    private String id;

    private Integer floorNumber;

    @Enumerated(EnumType.STRING)
    private BedType type;

    @Enumerated(EnumType.STRING)
    private BedStatus status;
}
