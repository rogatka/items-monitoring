package com.items.monitoring.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document("items")
@Data
@Builder
public class Item implements Serializable {

    @Id
    private final String id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private String code;

    private String name;

    private BigDecimal lastPrice;

    private String operatingSystem;

    private DisplayInfo displayInfo;

    private String cpu;

    private String storageSize;

    private String ramSize;

    private List<PriceHistoryElem> priceHistory;

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
