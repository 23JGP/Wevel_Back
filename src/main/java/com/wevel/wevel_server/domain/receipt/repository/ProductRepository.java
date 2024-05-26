package com.wevel.wevel_server.domain.receipt.repository;

import com.wevel.wevel_server.domain.receipt.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByReceipt_ReceiptId(Long receiptId);
    // 추가적인 쿼리 메서드가 필요하다면 여기에 추가 가능
    void deleteById(Long productId);
}