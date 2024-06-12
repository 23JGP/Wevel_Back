package com.wevel.wevel_server.domain.ocr;

import com.wevel.wevel_server.domain.ocr.service.OcrService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ocr")
@Tag(name = "Ocr", description = "이미지 택스트 변환 API")
public class OcrController {

    private final OcrService ocrService;

    @Autowired
    public OcrController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping("/convert")
    public String convertImageToText(@RequestParam("file") MultipartFile file) {
        return ocrService.convertImageToText(file);
    }
}
