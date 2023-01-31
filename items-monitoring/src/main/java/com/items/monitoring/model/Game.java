package com.items.monitoring.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("games")
@Getter
@Setter
@ToString
public class Game extends Item implements Serializable {

    private Long platformId;

    private String name;

    private String releaseDate;

    private Integer lastRating;

    private List<RatingHistoryElem> ratingHistory;

    public void addRatingHistory(RatingHistoryElem ratingHistoryElem) {
        if (ratingHistory == null) {
            ratingHistory = new ArrayList<>();
        }
        ratingHistory.add(ratingHistoryElem);
    }

    @Data
    @Builder
    public static class RatingHistoryElem implements Serializable{

        @CreatedDate
        private Instant date;

        private Integer rating;
    }
}
