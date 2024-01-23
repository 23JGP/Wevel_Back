package com.wevel.wevel_server.receipt;

import com.wevel.wevel_server.receipt.dto.ProductDTO;
import com.wevel.wevel_server.receipt.entity.Product;
import com.wevel.wevel_server.receipt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 상품 목록 조회
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        // Product 엔티티를 ProductDTO로 변환하여 반환
        List<Product> products = productService.getAllProducts();
        return products.stream()
                .map(product -> new ProductDTO(product.getProductName(), product.getQuantity(), product.getPrice()))
                .collect(Collectors.toList());
    }

    // 상품 추가
    @PostMapping("/add")
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO) {
        // ProductDTO를 Product 엔티티로 변환하여 서비스로 전달
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());

        Product addedProduct = productService.addProduct(product);

        // 서비스에서 반환된 Product 엔티티를 ProductDTO로 변환하여 반환
        return new ProductDTO(addedProduct.getProductName(), addedProduct.getQuantity(), addedProduct.getPrice());
    }

    // 여러 상품을 추가하는 기능
    @PostMapping("/add-multiple")
    public List<ProductDTO> addMultipleProducts(@RequestBody List<ProductDTO> productDTOs) {
        // 각 ProductDTO를 Product 엔티티로 변환하여 서비스로 전달하고 추가된 상품들을 다시 DTO로 변환하여 반환
        return productDTOs.stream()
                .map(productDTO -> {
                    Product product = new Product();
                    product.setProductName(productDTO.getProductName());
                    product.setQuantity(productDTO.getQuantity());
                    product.setPrice(productDTO.getPrice());

                    Product addedProduct = productService.addProduct(product);

                    return new ProductDTO(addedProduct.getProductName(), addedProduct.getQuantity(), addedProduct.getPrice());
                })
                .collect(Collectors.toList());
    }


    // 상품 수정
    @PostMapping("/edit/{productId}")
    public Product editProduct(@PathVariable Long productId, @RequestBody ProductDTO editedProductDTO) {
        // ProductDTO를 Product 엔티티로 변환하여 서비스로 전달
        Product editedProduct = new Product();
        editedProduct.setProductName(editedProductDTO.getProductName());
        editedProduct.setQuantity(editedProductDTO.getQuantity());
        editedProduct.setPrice(editedProductDTO.getPrice());

        // 서비스에서 반환된 수정된 Product 엔티티를 ProductDTO로 변환하여 반환
        return productService.editProduct(productId, editedProduct);
    }

    // 상품 목록을 JSON 문자열 리스트로 생성
    @PostMapping("/list-products")
    public List<String> createProductList(@RequestBody List<ProductDTO> productDTOS) {
        // 각 상품을 JSON으로 변환하여 리스트에 추가
        return productDTOS.stream()
                .map(productDTO -> String.format("{\"productName\": \"%s\", \"quantity\": %d, \"price\": %.2f}",
                        productDTO.getProductName(), productDTO.getQuantity(), productDTO.getPrice()))
                .collect(Collectors.toList());
    }

    // 여러 상품을 추가하는 기능
    @PostMapping("/add-multiple")
    public List<ProductDTO> addMultipleProducts(@RequestBody List<ProductDTO> productDTOs) {
        // 각 ProductDTO를 Product 엔티티로 변환하여 서비스로 전달하고 추가된 상품들을 다시 DTO로 변환하여 반환
        return productDTOs.stream()
                .map(productDTO -> {
                    Product product = new Product();
                    product.setProductName(productDTO.getProductName());
                    product.setQuantity(productDTO.getQuantity());
                    product.setPrice(productDTO.getPrice());

                    Product addedProduct = productService.addProduct(product);

                    return new ProductDTO(addedProduct.getProductName(), addedProduct.getQuantity(), addedProduct.getPrice());
                })
                .collect(Collectors.toList());
    }
}
