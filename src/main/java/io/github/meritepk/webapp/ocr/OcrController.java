package io.github.meritepk.webapp.ocr;

import java.io.InputStream;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/ocr")
public class OcrController {

    private final OcrService service;

    public OcrController(OcrService service) {
        this.service = service;
    }

    @PostMapping(value = "/img2txt")
    public ResponseEntity<Map<String, String>> extract(@RequestParam(name = "image") MultipartFile image,
            @RequestParam(name = "language", defaultValue = "eng") String language,
            @RequestParam(name = "mode", defaultValue = "") String mode) throws Exception {
        try (InputStream input = image.getInputStream()) {
            return ResponseEntity.ok(Map.of("text", service.extract(input, language, mode)));
        }
    }
}
