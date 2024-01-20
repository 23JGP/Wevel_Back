package com.wevel.wevel_server.receipt.service;

import com.wevel.wevel_server.receipt.dto.ReceiptDTO;
import com.wevel.wevel_server.receipt.entity.Receipt;
import com.wevel.wevel_server.receipt.repository.ReceiptRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public ReceiptDTO createReceipt(ReceiptDTO receiptDTO) {
        Receipt receipt = new Receipt();
        BeanUtils.copyProperties(receiptDTO, receipt);
        Receipt savedReceipt = receiptRepository.save(receipt);
        ReceiptDTO savedReceiptDTO = new ReceiptDTO();
        BeanUtils.copyProperties(savedReceipt, savedReceiptDTO);
        return savedReceiptDTO;
    }

    public ReceiptDTO getReceiptById(Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found with id: " + receiptId));
        ReceiptDTO receiptDTO = new ReceiptDTO();
        BeanUtils.copyProperties(receipt, receiptDTO);
        return receiptDTO;
    }

    public List<ReceiptDTO> getAllReceipts() {
        List<Receipt> receiptList = receiptRepository.findAll();
        return receiptList.stream()
                .map(receipt -> {
                    ReceiptDTO receiptDTO = new ReceiptDTO();
                    BeanUtils.copyProperties(receipt, receiptDTO);
                    return receiptDTO;
                })
                .collect(Collectors.toList());
    }

    // Other service methods

}
