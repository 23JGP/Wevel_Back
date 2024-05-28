package com.wevel.wevel_server.domain.ocr.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class OcrService {

    public String convertImageToText(MultipartFile file) {
        try {
            // 저장할 임시 파일 생성
            Path tempFile = Files.createTempFile("temp-", ".png");

            // 업로드된 이미지를 임시 파일로 복사
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            // Tesseract OCR 설정
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("C://Tess4J/tessdata"); // tessdata 디렉터리의 경로 설정
            tesseract.setLanguage("jpn"); // 언어 설정
            tesseract.setTessVariable("user_defined_dpi", "300"); // 예: 300 DPI로 설정

            // 이미지에서 텍스트 추출
            String result = tesseract.doOCR(tempFile.toFile());

            // 임시 파일 삭제
            Files.delete(tempFile);

            return result;
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
            return "Error during OCR processing: " + e.getMessage();
        }
    }
}
