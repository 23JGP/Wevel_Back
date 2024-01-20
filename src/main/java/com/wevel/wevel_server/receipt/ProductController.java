package com.wevel.wevel_server.receipt;

import com.wevel.wevel_server.receipt.dto.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProductController {

    @PostMapping("/list-products")
    public List<String> createProductList(@RequestBody List<Product> products) {
        // 각 상품을 JSON으로 변환하여 리스트에 추가
        List<String> productList = products.stream()
                .map(product -> String.format("{\"productName\": \"%s\", \"quantity\": %d, \"price\": %.2f}",
                        product.getProductName(), product.getQuantity(), product.getPrice()))
                .collect(Collectors.toList());

        return productList;
    }
}
