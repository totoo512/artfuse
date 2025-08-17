package com.zyy.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageSelectVO implements Serializable {
    
    /**
     * 选择记录ID
     */
    private Long selectionId;
}