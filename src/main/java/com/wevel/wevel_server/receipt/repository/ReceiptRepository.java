package com.wevel.wevel_server.receipt.repository;

import com.wevel.wevel_server.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    // 추가적인 쿼리 메서드가 필요하다면 여기에 추가 가능
}