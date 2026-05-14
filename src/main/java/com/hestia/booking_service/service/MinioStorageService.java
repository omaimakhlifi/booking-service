package com.hestia.booking_service.service;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioStorageService implements IFileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Override
    public String saveFile(MultipartFile file) {
        try {
            // 1. Créer le bucket s'il n'existe pas
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 2. Nettoyage du nom de fichier
            String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image";
            String cleanedName = originalName.replaceAll("[^a-zA-Z0-9.]", "_");
            String fileName = UUID.randomUUID().toString() + "_" + cleanedName;

            // 3. Déterminer le Content-Type réel
            String contentType = file.getContentType();
            if (contentType == null || contentType.isBlank() || contentType.toLowerCase().contains("automatique")) {
                // Déduire depuis l'extension si possible
                String ext = originalName.toLowerCase();
                if (ext.endsWith(".jpg") || ext.endsWith(".jpeg")) contentType = "image/jpeg";
                else if (ext.endsWith(".png"))  contentType = "image/png";
                else if (ext.endsWith(".gif"))  contentType = "image/gif";
                else if (ext.endsWith(".webp")) contentType = "image/webp";
                else                            contentType = "application/octet-stream";
            }

            // 4. Upload vers MinIO
            byte[] bytes = file.getBytes();
            try (var inputStream = new java.io.ByteArrayInputStream(bytes)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(inputStream, bytes.length, -1)
                                .contentType(contentType)
                                .build()
                );
            }

            return fileName;
        } catch (Exception e) {
            System.err.println("Détail Erreur MinIO: " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'upload du fichier vers MinIO", e);
        }
    }

    @Override
    public String generatePresignedUrl(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return null;
        }
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(2, TimeUnit.HOURS) // L'URL expirera après 2 heures
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération de l'URL présignée", e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            System.out.println("Fichier supprimé de MinIO : " + fileName);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression sur MinIO : " + fileName, e);
        }
    }
}