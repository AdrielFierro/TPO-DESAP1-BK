package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
// import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.demo.repository.ImageRepository;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    private final String containerName = "images";

    public String uploadImage(String originalImageName, InputStream data, long length)
            throws IOException {
        try {

            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(
                    "DefaultEndpointsProtocol=https;AccountName=ratatouillesa;AccountKey=M2xQtxzfX447XFMHWsOjnQ+fQBxXCmwIaY2DfzP+DkwKtB0rWA10ljC6QDGZ9ybmt/zosjb8PrWm+AStLN0fcQ==;EndpointSuffix=core.windows.net")
                    .buildClient();

            BlobContainerClient blobcontainerclient = blobServiceClient.getBlobContainerClient(containerName);

            // String newImageName = UUID.randomUUID().toString()
            // + originalImageName.substring(originalImageName.lastIndexOf(""));

            String newImageName = UUID.randomUUID().toString()
                    + originalImageName;

            BlobClient blobClient = blobcontainerclient.getBlobClient(newImageName);
            blobClient.upload(data, length, true);
            return blobClient.getBlobUrl();

        } catch (Exception e) {
            return e.getMessage();

        }

    }

    // public ResponseEntity<BinaryData> downloadImage(String url) {
    // throw new UnsupportedOperationException("Unimplemented method
    // 'downloadImage'");
    // }

}
