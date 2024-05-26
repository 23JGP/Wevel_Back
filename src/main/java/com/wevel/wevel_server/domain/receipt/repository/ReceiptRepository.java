package com.wevel.wevel_server.domain.receipt.repository;

import com.wevel.wevel_server.domain.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByUserId(Long userId);
    List<Receipt> findByUserIdAndTripName(Long userId, String tripName);
}