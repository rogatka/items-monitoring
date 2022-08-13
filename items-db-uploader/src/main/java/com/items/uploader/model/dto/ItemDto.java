package com.items.uploader.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDto {

    private String code;

    private String name;

    private BigDecimal price;

    private String operatingSystem;

    private DisplayInfo displayInfo;

    private String cpu;

    private String storageSize;

    private String ramSize;


    @Data
    public static class DisplayInfo {

        private String type;

        private String resolution;
    }
}
