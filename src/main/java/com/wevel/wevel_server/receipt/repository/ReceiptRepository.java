package com.wevel.wevel_server.receipt.repository;

import com.wevel.wevel_server.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByUserId(Long userId);
    // 추가적인 쿼리 메서드가 필요하다면 여기에 추가 가능

}