package com.wevel.wevel_server.domain.receipt.repository;

import com.wevel.wevel_server.domain.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByUserId(Long userId);
    List<Receipt> findByUserIdAndTripId(Long userId, Long tripId);
    @Query("SELECT r FROM Receipt r WHERE r.tripId = :tripId")
    List<Receipt> findByTripId(@Param("tripId") Long tripId);

}