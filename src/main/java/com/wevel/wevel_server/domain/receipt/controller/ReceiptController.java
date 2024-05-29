package com.wevel.wevel_server.domain.receipt.controller;

import com.wevel.wevel_server.domain.memo.entity.Memo;
import com.wevel.wevel_server.domain.memo.repository.MemoRepository;
import com.wevel.wevel_server.domain.receipt.dto.ProductDTO;
import com.wevel.wevel_server.domain.receipt.dto.ReceiptDTO;
import com.wevel.wevel_server.domain.receipt.entity.Product;
import com.wevel.wevel_server.domain.receipt.entity.Receipt;
import com.wevel.wevel_server.domain.receipt.repository.ProductRepository;
import com.wevel.wevel_server.domain.receipt.repository.ReceiptRepository;
import com.wevel.wevel_server.domain.receipt.service.ReceiptService;
import com.wevel.wevel_server.domain.tripInfo.entity.TripInfo;
import com.wevel.wevel_server.domain.tripInfo.service.TripInfoService;
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

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<Receipt> saveReceipt(@RequestBody ReceiptDTO receiptDTO) {
        Receipt savedReceipt = receiptService.saveReceipt(receiptDTO);

        // TripInfo 리스트를 반환하도록 수정
        List<TripInfo> tripInfos = tripInfoService.findTripInfoByUserIdAndTripId(receiptDTO.getUserId(), receiptDTO.getTripId());

        TripInfo tripInfo;
        if (!tripInfos.isEmpty()) {
            // 리스트의 첫 번째 항목 사용
            tripInfo = tripInfos.get(0);
        } else {
            // TripInfo가 없는 경우 처리
            tripInfo = new TripInfo();
            tripInfo.setUserId(receiptDTO.getUserId());
            tripInfo.setTripId(receiptDTO.getTripId());
            tripInfo.setSpentAmount(0.0);
            tripInfo.setRemainingAmount(tripInfo.getTotalBudget());
        }

        double totalSpentAmount = tripInfo.getSpentAmount();
        for (ProductDTO productDTO : receiptDTO.getProductDTOList()) {
            // 상품 정보 처리
            totalSpentAmount += productDTO.getPrice();
        }

        // 업데이트된 값을 설정
        tripInfo.setSpentAmount(totalSpentAmount);
        tripInfo.setRemainingAmount(tripInfo.getTotalBudget() - totalSpentAmount);

        // TripInfo 엔티티 저장
        tripInfoService.saveTripInfo(tripInfo);

        return new ResponseEntity<>(savedReceipt, HttpStatus.CREATED);
    }



    @DeleteMapping("/{memoId}")
    public ResponseEntity<String> deleteMemo(@PathVariable Long memoId) {
        Optional<Memo> memoOptional = memoRepository.findById(memoId);
        if (memoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("메모를 찾을 수 없습니다");
        }

        Memo memo = memoOptional.get();
        deleteProductsByReceiptId(memo.getReceipt().getReceiptId());
        memoRepository.delete(memo);

        return ResponseEntity.ok("메모가 성공적으로 삭제되었습니다");
    }

    private void deleteProductsByReceiptId(Long receiptId) {
        List<Product> products = productRepository.findByReceipt_ReceiptId(receiptId);
        productRepository.deleteAll(products);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Receipt>> getReceiptsByUserId(@PathVariable Long userId) {
        List<Receipt> receipts = receiptService.getReceiptsByUserId(userId);
        return new ResponseEntity<>(receipts, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/trip/{tripId}")
    public ResponseEntity<List<Receipt>> getReceiptsByUserIdAndTripId(
            @PathVariable Long userId,
            @PathVariable Long tripId) {
        List<Receipt> receipts = receiptService.getReceiptsByUserIdAndTripId(userId, tripId);
        return new ResponseEntity<>(receipts, HttpStatus.OK);
    }

    @PutMapping("/{receiptId}")
    public ResponseEntity<Receipt> updateReceipt(@PathVariable Long receiptId, @RequestBody Receipt updatedReceipt) {
        Receipt updated = receiptService.updateReceipt(receiptId, updatedReceipt);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{receiptId}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable Long receiptId) {
        receiptService.deleteReceipt(receiptId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
