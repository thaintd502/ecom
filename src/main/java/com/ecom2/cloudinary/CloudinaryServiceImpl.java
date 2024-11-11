package com.ecom2.cloudinary;

import com.cloudinary.Cloudinary;
import com.ecom2.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile multipartFile){
        try{
            return cloudinary.uploader()
                    .upload(multipartFile.getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();
        }catch(Exception e){
            throw new ResourceNotFoundException("Image", "url image", "url");
        }

    }
}
