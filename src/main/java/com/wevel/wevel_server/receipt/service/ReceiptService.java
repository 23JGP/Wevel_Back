package com.wevel.wevel_server.receipt.service;

import com.wevel.wevel_server.memo.entity.Memo;
import com.wevel.wevel_server.memo.repository.MemoRepository;
import com.wevel.wevel_server.receipt.dto.ReceiptDTO;
import com.wevel.wevel_server.receipt.entity.Product;
import com.wevel.wevel_server.receipt.entity.Receipt;
import com.wevel.wevel_server.receipt.repository.ReceiptRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private MemoRepository memoRepository;

    @Transactional
    public Receipt saveReceipt(ReceiptDTO receiptDTO) {
        System.out.println("리시트DTO " + receiptDTO.getUserId());  // receiptDTO의 userId 출력
        // ReceiptDTO로부터 Receipt 엔티티 생성
        Receipt receiptEntity = convertToReceiptEntity(receiptDTO);

        // Receipt 및 연관된 Memo 엔티티 저장
        receiptEntity = receiptRepository.save(receiptEntity);
        System.out.println("리시트 " + receiptEntity.getUserId());  // 저장된 Receipt 엔티티의 userId 출력
        Receipt receipt = new Receipt();

        return receiptEntity;
    }

    private Receipt convertToReceiptEntity(ReceiptDTO receiptDTO) {
        // productDTOList에서 products 리스트 생성
        List<Product> productEntities = receiptDTO.getProductDTOList().stream()
                .map(productDTO -> {
                    Product productEntity = new Product();
                    productEntity.setProductName(productDTO.getProductName());
                    productEntity.setPrice(productDTO.getPrice());
                    productEntity.setQuantity(productDTO.getQuantity());
                    return productEntity;
                })
                .collect(Collectors.toList());

        // Memo 엔티티 생성
        Memo memoEntity = new Memo();
        memoEntity.setUserId(receiptDTO.getUserId());
        memoEntity.setTripName(receiptDTO.getTripName());
        memoEntity.setDate(receiptDTO.getDate());
        memoEntity.setAmountReceived(receiptDTO.getReceivedMemos());
        memoEntity.setAmountGiven(receiptDTO.getGivenMemos());
        memoEntity.setRcompleted(false);
        memoEntity.setGcompleted(false);

        // Receipt 엔티티 생성 (연관된 상품 및 메모 설정)
        Receipt receiptEntity = Receipt.builder()
                .userId(receiptDTO.getUserId())
                .date(receiptDTO.getDate())
                .tripName(receiptDTO.getTripName())
                .title(receiptDTO.getTitle())
                .products(productEntities)
                .tax(receiptDTO.getTax())
                .memo(memoEntity)  // 연관된 Memo 엔티티 설정
                .build();


        return receiptEntity;
    }
}
