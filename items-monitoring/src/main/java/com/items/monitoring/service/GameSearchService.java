package com.items.monitoring.service;

import com.items.monitoring.model.Game;
import com.items.monitoring.repository.elasticsearch.ElasticsearchGameRepository;
import com.items.monitoring.repository.mongo.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Slf4j
@Service
@RequiredArgsConstructor
public class GameSearchService {

	private static final int PAGE_SIZE = 5;
	private static final int PAGE_NUMBER = 0;

	private final ElasticsearchGameRepository elasticsearchGameRepository;
	private final GameRepository gameRepository;

	public Flux<Game> processSearch(final String query) {
		log.info("Search with query {}", query);
		// search in elasticsearch
		return elasticsearchGameRepository.findFuzzyByName(query)
				// if empty, go to mongo
				.switchIfEmpty(s -> gameRepository.findByNameLike(query)
						// if found, save to elasticsearch so next time value will be retrieved from elasticsearch
						.doOnNext(e -> elasticsearchGameRepository.save(e).subscribe())
						.subscribe(s));
	}

	public Flux<String> fetchSuggestions(String query) {
		return elasticsearchGameRepository.findByNameLike(query, PageRequest.of(PAGE_NUMBER, PAGE_SIZE))
				.map(Game::getName);
	}
}