package com.items.monitoring.web;

import com.items.monitoring.model.Game;
import com.items.monitoring.model.Smartphone;
import com.items.monitoring.service.GameSearchService;
import com.items.monitoring.service.SmartphoneSearchService;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Observed
@Tag(name = "Fuzy Search API")
@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SmartphoneSearchService smartphoneSearchService;

    private final GameSearchService gameSearchService;

    @GetMapping("/smartphones")
    public Flux<Smartphone> fetchSmartphonesByName(@RequestParam(value = "q", required = false) String query) {
        log.info("searching by name {}", query);
        Flux<Smartphone> smartphones = smartphoneSearchService.processSearch(query);
        log.info("smartphones {}", smartphones);
        return smartphones;
    }

	@GetMapping("/games")
	public Flux<Game> fetchGamesByName(@RequestParam(value = "q", required = false) String query) {
		log.info("searching by name {}", query);
        Flux<Game> games = gameSearchService.processSearch(query);
		log.info("games {}", games);
		return games;
	}

    @GetMapping("/suggestions/smartphones")
    public Flux<String> fetchSmartphoneSuggestions(@RequestParam(value = "q", required = false) String query) {
        log.info("fetch suggests {}", query);
        Flux<String> suggests = smartphoneSearchService.fetchSuggestions(query);
        log.info("suggests {}", suggests);
        return suggests;
    }

    @GetMapping(value = "/suggestions/games")
	public Mono<List<String>> fetchGameSuggestions(@RequestParam(value = "q", required = false) String query) {
		log.info("fetch suggests {}", query);
        Mono<List<String>> suggests = gameSearchService.fetchSuggestions(query)
                .collectList();
		log.info("suggests {}", suggests);
		return suggests;
	}
}
