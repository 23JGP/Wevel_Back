package com.wevel.wevel_server.controller;
import com.wevel.wevel_server.dto.ReceiptDTO;
import com.wevel.wevel_server.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<ReceiptDTO> createReceipt(@RequestBody ReceiptDTO receiptDTO) {
        ReceiptDTO createdReceipt = receiptService.createReceipt(receiptDTO);
        return new ResponseEntity<>(createdReceipt, HttpStatus.CREATED);
    }

    @GetMapping("/{receiptId}")
    public ResponseEntity<ReceiptDTO> getReceiptById(@PathVariable Long receiptId) {
        ReceiptDTO receiptDTO = receiptService.getReceiptById(receiptId);
        return new ResponseEntity<>(receiptDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReceiptDTO>> getAllReceipts() {
        List<ReceiptDTO> receiptDTOList = receiptService.getAllReceipts();
        return new ResponseEntity<>(receiptDTOList, HttpStatus.OK);
    }

}