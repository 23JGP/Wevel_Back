package com.wevel.wevel_server.domain.receipt;
import com.wevel.wevel_server.domain.memo.entity.Memo;
import com.wevel.wevel_server.domain.memo.repository.MemoRepository;
import com.wevel.wevel_server.domain.receipt.dto.ProductDTO;
import com.wevel.wevel_server.domain.receipt.dto.ReceiptDTO;
import com.wevel.wevel_server.domain.receipt.entity.Product;
import com.wevel.wevel_server.domain.receipt.repository.ProductRepository;
import com.wevel.wevel_server.domain.receipt.repository.ReceiptRepository;
import com.wevel.wevel_server.domain.receipt.service.ReceiptService;
import com.wevel.wevel_server.domain.tripInfo.entity.TripInfo;
import com.wevel.wevel_server.domain.tripInfo.service.TripInfoService;
import com.wevel.wevel_server.domain.receipt.entity.Receipt;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/receipts")
@Tag(name = "Receipt", description = "영수증 관련 API")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private TripInfoService tripInfoService;
    // post : /api/receipts
    @PostMapping
    public ResponseEntity<Receipt> saveReceipt(@RequestBody ReceiptDTO receiptDTO) {
        Receipt savedReceipt = receiptService.saveReceipt(receiptDTO);
        // 상품들의 가격을 userId와 tripName이 같은 tripInfo에 추가
        for (ProductDTO productDTO : receiptDTO.getProductDTOList()) {
            TripInfo tripInfo = tripInfoService.findTripInfoByUserIdAndTripName(receiptDTO.getUserId(), receiptDTO.getTripName());
            if (tripInfo != null) {
                // 이미 존재하는 tripInfo에 가격을 추가
                tripInfo.setSpentAmount(tripInfo.getSpentAmount() + productDTO.getPrice());
                tripInfo.setRemainingAmount(tripInfo.getTotalBudget() - productDTO.getPrice());
                tripInfoService.saveTripInfo(tripInfo);
            } else {
                // 존재하지 않는 경우 새로운 tripInfo를 생성하여 가격을 추가
                TripInfo newTripInfo = new TripInfo();
                newTripInfo.setUserId(receiptDTO.getUserId());
                newTripInfo.setTripName(receiptDTO.getTripName());
                newTripInfo.setSpentAmount(productDTO.getQuantity() * productDTO.getPrice());
                tripInfoService.saveTripInfo(newTripInfo);
            }
        }
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