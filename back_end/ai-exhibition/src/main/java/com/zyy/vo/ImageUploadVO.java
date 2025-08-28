package com.zyy.vo;

import lombok.Data;

import java.util.List;

@Data
public class ImageUploadVO {
    private Long portraitImageId;
    private ImageVO portraitImage;
    private List<Long> exhibitionImageIds;
    private List<ImageVO> exhibitionImages;
}
