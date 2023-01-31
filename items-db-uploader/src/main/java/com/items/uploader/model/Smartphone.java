package com.items.uploader.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("smartphones")
@Getter
@Setter
@ToString
@Builder
public class Smartphone extends Item {

    private String code;

    private String name;

    private BigDecimal lastPrice;

    private String operatingSystem;

    private DisplayInfo displayInfo;

    private String cpu;

    private String storageSize;

    private String ramSize;

    private List<PriceHistoryElem> priceHistory;

    public void addPriceHistory(PriceHistoryElem priceHistoryElem) {
        if (priceHistory == null) {
            priceHistory = new ArrayList<>();
        }
        priceHistory.add(priceHistoryElem);
    }


    @Data
    @Builder
    public static class DisplayInfo {

        private String type;

        private String resolution;
    }

    @Data
    @Builder
    public static class PriceHistoryElem {

        @CreatedDate
        private Instant date;

        private BigDecimal price;
    }
}
