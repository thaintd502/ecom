package com.ecom2.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
}
