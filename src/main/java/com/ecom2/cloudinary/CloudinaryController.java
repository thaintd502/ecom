package com.ecom2.cloudinary;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CloudinaryController {


    private final CloudinaryService cloudinaryService;

    @GetMapping("/cloudinary/home2")
    public String home(){
        return "home2";
    }

    @PostMapping("/cloudinary/upload")
    public String uploadFile(@RequestParam("image") MultipartFile multipartFile,
                             Model model) throws IOException {
        String imageUrl = cloudinaryService.uploadFile(multipartFile);

        model.addAttribute("imageUrl", imageUrl);

        return "gallery";
    }
}
