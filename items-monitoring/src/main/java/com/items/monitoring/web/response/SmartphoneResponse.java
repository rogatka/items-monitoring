package com.items.monitoring.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmartphoneResponse {

    private String id;

    private String code;

    private String name;

    private BigDecimal lastPrice;

    private String operatingSystem;

    private DisplayInfo displayInfo;

    private String cpu;

    private String storageSize;

    private String ramSize;

    private List<SmartphoneResponse.PriceHistoryElem> priceHistory;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisplayInfo {

        private String type;

        private String resolution;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceHistoryElem implements Serializable {

        @CreatedDate
        private Instant date;

        private BigDecimal price;
    }
}
