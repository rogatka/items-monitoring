package com.items.parsing.client;

import com.items.parsing.client.model.GamesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "rawg-client",
        url = "${parsing.games.rawg.base-url}",
        configuration = RawgFeignClientConfiguration.class
)
public interface RawgFeignClient {

    @GetMapping(value = "/api/games")
    GamesResponse getGames(@RequestParam("page") Integer pageNumber,
                                 @RequestParam("page_size") Integer pageSize,
                                 @RequestParam("metacritic") String metacriticRatingRange,
                                 @RequestParam("dates") String dates,
                                 @RequestParam("ordering") String ordering);
}