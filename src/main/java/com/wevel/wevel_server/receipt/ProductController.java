package com.wevel.wevel_server.receipt;

import com.wevel.wevel_server.receipt.dto.ProductDTO;
import com.wevel.wevel_server.receipt.entity.Product;
import com.wevel.wevel_server.receipt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

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

    @PutMapping("/edit/{productId}")
    public ResponseEntity<ProductDTO> editProduct(@PathVariable Long productId, @RequestBody ProductDTO editedProductDTO) {
        // 서비스에서 반환된 수정된 ProductDTO를 ResponseEntity로 반환
        ProductDTO updatedProductDTO = productService.editProduct(productId, editedProductDTO);
        if (updatedProductDTO != null) {
            return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
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
}
