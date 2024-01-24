package com.wevel.wevel_server.receipt.repository;

import com.wevel.wevel_server.receipt.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 추가적인 쿼리 메서드가 필요하다면 여기에 추가 가능
    void deleteById(Long productId);
}