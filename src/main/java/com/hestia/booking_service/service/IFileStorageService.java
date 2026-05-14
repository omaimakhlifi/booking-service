package com.hestia.booking_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {

    String saveFile(MultipartFile file);
    String generatePresignedUrl(String fileName);
    void deleteFile(String fileName);

}
