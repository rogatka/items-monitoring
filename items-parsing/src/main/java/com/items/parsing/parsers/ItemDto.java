package com.items.parsing.parsers;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
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
    @Builder
    static class DisplayInfo {

        private String type;

        private String resolution;
    }
}
