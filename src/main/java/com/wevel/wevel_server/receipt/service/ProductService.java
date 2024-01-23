package com.wevel.wevel_server.receipt.service;

import com.wevel.wevel_server.receipt.entity.Product;
import com.wevel.wevel_server.receipt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Product editProduct(Long productId, Product editedProduct) {
        Product existingProduct = productRepository.findById(productId).orElse(null);
        if (existingProduct != null) {
            existingProduct.setProductName(editedProduct.getProductName());
            existingProduct.setQuantity(editedProduct.getQuantity());
            existingProduct.setPrice(editedProduct.getPrice());
            return productRepository.save(existingProduct);
        }
        return null;
    }
}
