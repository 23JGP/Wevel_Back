package com.wevel.wevel_server.domain.receipt.service;

import com.wevel.wevel_server.domain.receipt.dto.ProductDTO;
import com.wevel.wevel_server.domain.receipt.entity.Product;
import com.wevel.wevel_server.domain.receipt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // ProductService
//    public ProductDTO editProduct(Long productId, ProductDTO editedProductDTO) {
//        Product existingProduct = productRepository.findById(productId).orElse(null);
//        if (existingProduct != null) {
//            existingProduct.setProductName(editedProductDTO.getProductName());
//            existingProduct.setQuantity(editedProductDTO.getQuantity());
//            existingProduct.setPrice(editedProductDTO.getPrice());
//            Product updatedProduct = productRepository.save(existingProduct);
//
//            // 수정된 Product 엔티티를 ProductDTO로 변환하여 반환
//            return new ProductDTO(updatedProduct.getProductName(), updatedProduct.getQuantity(), updatedProduct.getPrice());
//        }
//        return null;
//    }
//    public void deleteProduct(Long productId) {
//        productRepository.deleteById(productId);
//    }

    public void updateProduct(ProductDTO productDTO) {
        Optional<Product> optionalProduct = productRepository.findById(productDTO.getProductId());
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();

            // 업데이트 로직 수행
            existingProduct.setProductName(productDTO.getProductName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setQuantity(productDTO.getQuantity());

            productRepository.save(existingProduct);
        } else {
            // 적절한 처리 (예외 처리 등)
        }
    }
}
