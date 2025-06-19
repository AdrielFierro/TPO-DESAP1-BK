package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.azure.core.util.BinaryData;

import com.example.demo.service.ImageService;
import java.io.IOException;

import java.io.InputStream;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageservice;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile file)
            throws IOException {
        try (InputStream inputStream = file.getInputStream()) {

            String imageurl = imageservice.uploadImage(file.getOriginalFilename().toString(), inputStream,
                    file.getSize());

            return ResponseEntity.ok(imageurl);
        }

    }

    @GetMapping("/download")
    public ResponseEntity<BinaryData> downloadFile(@RequestBody String url)
            throws IOException {

        return imageservice.downloadImage(url);

    }

}
