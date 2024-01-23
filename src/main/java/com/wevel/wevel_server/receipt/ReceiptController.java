package com.wevel.wevel_server.receipt;
import com.wevel.wevel_server.receipt.dto.ReceiptDTO;
import com.wevel.wevel_server.receipt.entity.Receipt;
import com.wevel.wevel_server.receipt.service.ReceiptService;
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
    public ResponseEntity<Receipt> saveReceipt(@RequestBody ReceiptDTO receiptDTO) {
        Receipt savedReceipt = receiptService.saveReceipt(receiptDTO);
        return new ResponseEntity<>(savedReceipt, HttpStatus.CREATED);
    }
}