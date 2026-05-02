package com.hospital.backend.repository;

import com.hospital.backend.entity.Bed;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface BedRepository extends JpaRepository<Bed, String> {
    List<Bed> findByFloorNumber(Integer floorNumber);
    Page<Bed> findByFloorNumberOrderByIdAsc(Integer floorNumber, Pageable pageable);
    List<Bed> findByFloorNumberAndIdGreaterThanOrderByIdAsc(Integer floorNumber, String id, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Bed b WHERE b.id = :id")
    Optional<Bed> findByIdWithLock(String id);
}
