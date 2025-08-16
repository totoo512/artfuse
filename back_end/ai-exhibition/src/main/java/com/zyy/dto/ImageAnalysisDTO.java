package com.zyy.dto;

import lombok.Data;

import java.util.List;

@Data
public class ImageAnalysisDTO {
    private Long portraitImageId;
    private List<Long> exhibitionImageIds;
}
