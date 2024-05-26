package com.wevel.wevel_server.domain.receipt;

import com.wevel.wevel_server.domain.receipt.service.ProductService;
import com.wevel.wevel_server.domain.receipt.service.ReceiptService;
import com.wevel.wevel_server.domain.memo.dto.MemoDTO;
import com.wevel.wevel_server.domain.memo.service.MemoService;
import com.wevel.wevel_server.domain.receipt.dto.ProductDTO;
import com.wevel.wevel_server.domain.receipt.dto.ReceiptDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UpdateController {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private MemoService memoService;

    @Autowired
    private ProductService productService;

    @PutMapping("/updateReceipt/{receiptId}")
    public ResponseEntity<String> updateReceipt(@PathVariable Long receiptId, @RequestBody ReceiptDTO receiptDTO) {
        receiptDTO.setReceiptId(receiptId);
        receiptService.updateReceipt(receiptDTO);
        return ResponseEntity.ok("영수증이 성공적으로 수정되었습니다.");
    }

    @PutMapping("/updateMemo/{memoId}")
    public ResponseEntity<String> updateMemo(@PathVariable Long memoId, @RequestBody MemoDTO memoDTO) {
        memoDTO.setMemoId(memoId);
        memoService.updateMemo(memoDTO);
        return ResponseEntity.ok("메모가 성공적으로 수정되었습니다.");
    }

    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        productDTO.setProductId(productId);
        productService.updateProduct(productDTO);
        return ResponseEntity.ok("상품이 성공적으로 수정되었습니다.");
    }
}