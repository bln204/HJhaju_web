package com.hjhaju_web.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateUUID {
        public static String generateId() {
            // UUID là 128-bit -> hex 32 ký tự (có gạch nối)
            String uuid = UUID.randomUUID().toString().replace("-", ""); // 32 ký tự hex
            return uuid.substring(0, 24); // cắt còn 24 ký tự
        }
}
