package com.wevel.wevel_server.receipt;
import com.wevel.wevel_server.memo.entity.Memo;
import com.wevel.wevel_server.memo.repository.MemoRepository;
import com.wevel.wevel_server.receipt.dto.ReceiptDTO;
import com.wevel.wevel_server.receipt.entity.Product;
import com.wevel.wevel_server.receipt.entity.Receipt;
import com.wevel.wevel_server.receipt.repository.ProductRepository;
import com.wevel.wevel_server.receipt.repository.ReceiptRepository;
import com.wevel.wevel_server.receipt.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<Receipt> saveReceipt(@RequestBody ReceiptDTO receiptDTO) {
        Receipt savedReceipt = receiptService.saveReceipt(receiptDTO);
        return new ResponseEntity<>(savedReceipt, HttpStatus.CREATED);
    }

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ProductRepository productRepository;

    // http://localhost:8080/api/receipts/:id
    @DeleteMapping("/deleteMemo/{memoId}")
    public ResponseEntity<String> deleteMemo(@PathVariable Long memoId) {
        // Memo 엔티티 로드
        Optional<Memo> memoOptional = memoRepository.findById(memoId);
        if (!memoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("메모를 찾을 수 없습니다");
        }

        Memo memo = memoOptional.get();
        // Receipt와 연관된 Product 삭제
        deleteProductsByReceiptId(memo.getReceipt().getReceiptId());

        // Memo 삭제
        memoRepository.delete(memo);

        return ResponseEntity.ok("메모가 성공적으로 삭제되었습니다");
    }

    private void deleteProductsByReceiptId(Long receiptId) {
        // ReceiptId에 해당하는 Product들 로드
        List<Product> products = productRepository.findByReceipt_ReceiptId(receiptId);

        // Product 삭제
        productRepository.deleteAll(products);
    }

//    특정 userId에 해당하는 모든 Receipt 불러오기 (GET API):
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Receipt>> getReceiptsByUserId(@PathVariable Long userId) {
        List<Receipt> receipts = receiptService.getReceiptsByUserId(userId);
        return new ResponseEntity<>(receipts, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/trip/{tripName}")
    public ResponseEntity<List<Receipt>> getReceiptsByUserIdAndTripName(
            @PathVariable Long userId,
            @PathVariable String tripName) {
        List<Receipt> receipts = receiptService.getReceiptsByUserIdAndTripName(userId, tripName);
        return new ResponseEntity<>(receipts, HttpStatus.OK);
    }


    //    Receipt 수정하기 (PUT API):
    @PutMapping("/{receiptId}")
    public ResponseEntity<Receipt> updateReceipt(@PathVariable Long receiptId, @RequestBody Receipt updatedReceipt) {
        Receipt updated = receiptService.updateReceipt(receiptId, updatedReceipt);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

//    Receipt 삭제하기 (DELETE API):
    @DeleteMapping("/{receiptId}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable Long receiptId) {
        receiptService.deleteReceipt(receiptId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}