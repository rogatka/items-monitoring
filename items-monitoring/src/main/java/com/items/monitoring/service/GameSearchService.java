package com.items.monitoring.service;

import com.items.monitoring.model.Game;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class GameSearchService {

	private static final String GAMES_INDEX = "index_games";

	private final ElasticsearchOperations elasticsearchOperations;

	public List<Game> processSearch(final String query) {
		log.info("Search with query {}", query);

		// 1. Create query on multiple fields enabling fuzzy search
		QueryBuilder queryBuilder =
				QueryBuilders
				.multiMatchQuery(query, "name")
				.fuzziness(Fuzziness.AUTO);

		Query searchQuery = new NativeSearchQueryBuilder()
				                .withFilter(queryBuilder)
				                .build();

		// 2. Execute search
		SearchHits<Game> gameHits =
				elasticsearchOperations
				.search(searchQuery, Game.class, IndexCoordinates.of(GAMES_INDEX));

		// 3. Map searchHits to games list
		return gameHits.stream()
				.map(SearchHit::getContent)
				.map(Game.class::cast)
				.toList();
	}

	public List<String> fetchSuggestions(String query) {
		QueryBuilder queryBuilder = QueryBuilders
				.wildcardQuery("name", query+"*");

		Query searchQuery = new NativeSearchQueryBuilder()
				.withFilter(queryBuilder)
				.withPageable(PageRequest.of(0, 5))
				.build();

		SearchHits<Game> searchSuggestions =
				elasticsearchOperations.search(searchQuery, Game.class, IndexCoordinates.of(GAMES_INDEX));

		return searchSuggestions.stream()
				.map(SearchHit::getContent)
                .map(Game.class::cast)
                .map(Game::getName)
				.toList();
	}
}