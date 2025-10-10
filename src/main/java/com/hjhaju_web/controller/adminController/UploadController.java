package com.hjhaju_web.controller.adminController;

import com.hjhaju_web.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/files/upload")
public class UploadController {

   @Autowired
    private UploadFileService uploadFileService;

    @PostMapping(produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        List<String> dataImages = new ArrayList<>();

        if (file == null || file.isEmpty()) {
            return ResponseEntity.ok(""); // Không có file → trả về chuỗi rỗng
        }
        try {
            // Lưu file vào thư mục uploads (ví dụ: D:/BoxChat_Java/file-upload/)
            String fileName = uploadFileService.uploadFile(file, "file-upload");
            dataImages.add(fileName);
            return ResponseEntity.ok(fileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }
}

