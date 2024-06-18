package com.wevel.wevel_server.domain.receipt.service;

import com.wevel.wevel_server.domain.memo.entity.Memo;
import com.wevel.wevel_server.domain.memo.repository.MemoRepository;
import com.wevel.wevel_server.domain.receipt.dto.ReceiptDTO;
import com.wevel.wevel_server.domain.receipt.dto.ReceiptResponse;
import com.wevel.wevel_server.domain.receipt.entity.Product;
import com.wevel.wevel_server.domain.receipt.entity.Receipt;
import com.wevel.wevel_server.domain.receipt.repository.ReceiptRepository;
import com.wevel.wevel_server.domain.tripInfo.entity.TripInfo;
import com.wevel.wevel_server.domain.tripInfo.repository.TripInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private TripInfoRepository tripInfoRepository;



    private void updateTripInfo(TripInfo tripInfo, Receipt receipt) {
        // 여행 정보의 총 사용 금액 및 남은 예산 계산
        double totalSpentAmount = tripInfo.getSpentAmount() != 0 ? tripInfo.getSpentAmount() : 0;
        double totalBudget = tripInfo.getTotalBudget() != 0 ? tripInfo.getTotalBudget() : 0;

        for (Product product : receipt.getProducts()) {
            totalSpentAmount += product.getPrice();
        }

        double remainingAmount = totalBudget - totalSpentAmount;

        // 여행 정보 업데이트
        tripInfo.setSpentAmount(totalSpentAmount);
        tripInfo.setRemainingAmount(remainingAmount);

        // 로그 추가
        System.out.println("Total Spent Amount: " + totalSpentAmount);
        System.out.println("Remaining Amount: " + remainingAmount);

        tripInfoRepository.save(tripInfo);
    }

//    public void updateTripInfoWithReceipts(Long userId, String tripName) {
//        // 단계 1: 특정 사용자 및 여행에 대한 영수증 검색
//        List<Receipt> receipts = receiptRepository.findByUserIdAndTripName(userId, tripName);
//
//        // 단계 2: 각 영수증의 제품에서 가격 합산
//        double totalSpentAmount = receipts.stream()
//                .flatMap(receipt -> receipt.getProducts().stream())
//                .mapToDouble(Product::getPrice) // 수정된 부분
//                .sum();
//
//        // 단계 3: TripInfo 엔터티의 spentAmount 및 remainingAmount 업데이트
//        TripInfo tripInfo = tripInfoRepository.findByUserIdAndTripName(userId, tripName);
//        double totalRemainingAmount = tripInfo.getTotalBudget() - totalSpentAmount;
//
//        tripInfo.setSpentAmount(totalSpentAmount);
//        tripInfo.setRemainingAmount(totalRemainingAmount);
//
//        tripInfoRepository.save(tripInfo);
//    }


    public Receipt saveReceipt(ReceiptDTO receiptDTO) {
        log.info("ReceiptDTO userId: {}", receiptDTO.getUserId());
        try {
            // ReceiptDTO로부터 Receipt 엔티티 생성
            Receipt receiptEntity = convertToReceiptEntity(receiptDTO);

            // Receipt 및 연관된 Memo 엔티티 저장
            receiptEntity = receiptRepository.save(receiptEntity);

            // Memo 엔터티와 Receipt 엔터티 연결 설정
            Memo memoEntity = receiptEntity.getMemo();
            memoEntity.setReceipt(receiptEntity);
            memoRepository.save(memoEntity);

            log.info("Saved Receipt entity with userId: {}", receiptEntity.getUserId());

            return receiptEntity;
        } catch (Exception e) {
            log.error("Error while saving receipt", e);
            throw e;
        }

    }

    private Receipt convertToReceiptEntity(ReceiptDTO receiptDTO) {
        // Product 엔터티 리스트 생성
        List<Product> productEntities = receiptDTO.getProductDTOList().stream()
                .map(productDTO -> {
                    Product productEntity = new Product();
                    productEntity.setProductName(productDTO.getProductName());
                    productEntity.setPrice(productDTO.getPrice());
                    productEntity.setQuantity(productDTO.getQuantity());
                    productEntity.setReceipt(null);  // 나중에 연결 설정
                    return productEntity;
                })
                .collect(Collectors.toList());

        // Memo 엔터티 생성
        Memo memoEntity = new Memo();
        memoEntity.setUserId(receiptDTO.getUserId());
        memoEntity.setTripId(receiptDTO.getTripId());
        memoEntity.setDate(receiptDTO.getDate());
        memoEntity.setAmountReceived(receiptDTO.getReceivedMemos());
        memoEntity.setAmountGiven(receiptDTO.getGivenMemos());
        memoEntity.setRcompleted(false);
        memoEntity.setGcompleted(false);

        // Receipt 엔터티 생성 (연관된 상품 및 메모 설정)
        Receipt receiptEntity = Receipt.builder()
                .userId(receiptDTO.getUserId())
                .date(receiptDTO.getDate())
                .tripId(receiptDTO.getTripId())
                .title(receiptDTO.getTitle())
                .products(productEntities)
                .tax(receiptDTO.getTax())
                .memo(memoEntity)  // 연관된 Memo 엔터티 설정
                .build();

        // Product 엔터티와 Receipt 엔터티 연결 설정
        for (Product product : productEntities) {
            product.setReceipt(receiptEntity);
        }

        return receiptEntity;
    }

    public List<Receipt> getReceiptsByUserId(Long userId) {
        return receiptRepository.findByUserId(userId);
    }

    public Receipt updateReceipt(Long receiptId, Receipt updatedReceipt) {
        Receipt existingReceipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new EntityNotFoundException("Receipt not found with id: " + receiptId));

        existingReceipt.setTripId(updatedReceipt.getTripId());
        existingReceipt.setTitle(updatedReceipt.getTitle());
        existingReceipt.setProducts(updatedReceipt.getProducts());
        existingReceipt.setMemo(updatedReceipt.getMemo());
        existingReceipt.setTax(updatedReceipt.getTax());
        existingReceipt.setDate(updatedReceipt.getDate());

        return receiptRepository.save(existingReceipt);
    }

    public void deleteReceipt(Long receiptId) {
        receiptRepository.deleteById(receiptId);
    }

    public void updateReceipt(ReceiptDTO receiptDTO) {
        Optional<Receipt> optionalReceipt = receiptRepository.findById(receiptDTO.getReceiptId());
        if (optionalReceipt.isPresent()) {
            Receipt existingReceipt = optionalReceipt.get();

            // 업데이트 로직 수행
            existingReceipt.setUserId(receiptDTO.getUserId());
            existingReceipt.setTripId(receiptDTO.getTripId());
            existingReceipt.setTitle(receiptDTO.getTitle());

            receiptRepository.save(existingReceipt);
        } else {
            // 적절한 처리 (예외 처리 등)
        }
    }

    public List<Receipt> getReceiptsByUserIdAndTripId(Long userId, Long tripId) {
        return receiptRepository.findByUserIdAndTripId(userId, tripId);
    }

    public List<ReceiptResponse> getReceiptsByTripId(Long tripId) {
        List<Receipt> receipts = receiptRepository.findByTripId(tripId);
        List<ReceiptResponse> responses = new ArrayList<>();

        for (Receipt receipt : receipts) {
            ReceiptResponse response = new ReceiptResponse();
            response.setTripId(receipt.getTripId());
            response.setReceiptId(receipt.getReceiptId());
            response.setTitle(receipt.getTitle());
            response.setDate(formatDate(receipt.getDate()));
            response.setTotal(calculateTotal(receipt.getProducts()));
            responses.add(response);
        }

        return responses;
    }

    private Double calculateTotal(List<Product> products) {
        Double total = 0.0;
        for (Product product : products) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd");
        return sdf.format(date);
    }
}
