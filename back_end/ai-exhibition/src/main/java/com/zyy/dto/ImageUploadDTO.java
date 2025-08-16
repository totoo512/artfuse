package com.zyy.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImageUploadDTO {
    MultipartFile portraitImage;
    List<MultipartFile> exhibitionImages;
}
