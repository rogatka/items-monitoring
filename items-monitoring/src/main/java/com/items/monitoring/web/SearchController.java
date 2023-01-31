package com.items.monitoring.web;

import com.items.monitoring.model.Game;
import com.items.monitoring.model.Smartphone;
import com.items.monitoring.service.GameSearchService;
import com.items.monitoring.service.SmartphoneSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Fuzy Search API")
@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SmartphoneSearchService smartphoneSearchService;

    private final GameSearchService gameSearchService;

    @GetMapping("/smartphones")
    public List<Smartphone> fetchSmartphonesByName(@RequestParam(value = "q", required = false) String query) {
        log.info("searching by name {}", query);
        List<Smartphone> smartphones = smartphoneSearchService.processSearch(query);
        log.info("smartphones {}", smartphones);
        return smartphones;
    }

	@GetMapping("/games")
	public List<Game> fetchGamesByName(@RequestParam(value = "q", required = false) String query) {
		log.info("searching by name {}", query);
		List<Game> games = gameSearchService.processSearch(query);
		log.info("games {}", games);
		return games;
	}

    @GetMapping("/suggestions/smartphones")
    public List<String> fetchSmartphoneSuggestions(@RequestParam(value = "q", required = false) String query) {
        log.info("fetch suggests {}", query);
        List<String> suggests = smartphoneSearchService.fetchSuggestions(query);
        log.info("suggests {}", suggests);
        return suggests;
    }

	@GetMapping("/suggestions/games")
	public List<String> fetchGameSuggestions(@RequestParam(value = "q", required = false) String query) {
		log.info("fetch suggests {}", query);
		List<String> suggests = gameSearchService.fetchSuggestions(query);
		log.info("suggests {}", suggests);
		return suggests;
	}
}
