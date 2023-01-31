package com.items.monitoring.web.response;

import com.items.monitoring.model.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

    private String id;

    private String name;

    private String releaseDate;

    private Integer lastRating;

    private List<Game.RatingHistoryElem> ratingHistory;

    public void addRatingHistory(Game.RatingHistoryElem ratingHistoryElem) {
        if (ratingHistory == null) {
            ratingHistory = new ArrayList<>();
        }
        ratingHistory.add(ratingHistoryElem);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingHistoryElem implements Serializable{

        @CreatedDate
        private Instant date;

        private Integer rating;
    }
}
