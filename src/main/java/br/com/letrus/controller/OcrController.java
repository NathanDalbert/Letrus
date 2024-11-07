package br.com.letrus.controller;

import br.com.letrus.service.GoogleVisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    private final GoogleVisionService visionService;

    @Autowired
    public OcrController(GoogleVisionService visionService) {
        this.visionService = visionService;
    }
    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/detect-text")
    public ResponseEntity<String> detectText(@RequestParam("file") MultipartFile file) {
        try {
            // Salvar o arquivo temporariamente para enviar à API
            File tempFile = File.createTempFile("upload", file.getOriginalFilename());
            file.transferTo(tempFile);

            // Enviar o arquivo para o serviço OCR
            String detectedText = visionService.requestOCR(tempFile);

            // Deletar o arquivo temporário
            tempFile.delete();

            return ResponseEntity.ok(detectedText);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar imagem.");
        }
    }
}
