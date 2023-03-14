package com.items.monitoring.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("smartphones")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "index_smartphones")
@Getter
@Setter
@ToString
public class Smartphone extends Item implements Serializable {

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
    public static class DisplayInfo implements Serializable {

        private String type;

        private String resolution;
    }

    @Data
    @Builder
    public static class PriceHistoryElem implements Serializable {

        @CreatedDate
        private Instant date;

        private BigDecimal price;
    }
}
