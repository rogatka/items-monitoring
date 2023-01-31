package com.items.monitoring.service;

import com.items.monitoring.model.Smartphone;
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
public class SmartphoneSearchService {

	private static final String SMARTPHONES_INDEX = "index_smartphones";

	private final ElasticsearchOperations elasticsearchOperations;

	public List<Smartphone> processSearch(final String query) {
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
		SearchHits<Smartphone> smartphoneHits =
				elasticsearchOperations
				.search(searchQuery, Smartphone.class, IndexCoordinates.of(SMARTPHONES_INDEX));

		// 3. Map searchHits to smartphones list
		return smartphoneHits.stream()
				.map(SearchHit::getContent)
				.map(Smartphone.class::cast)
				.toList();
	}

	public List<String> fetchSuggestions(String query) {
		QueryBuilder queryBuilder = QueryBuilders
				.wildcardQuery("name", query+"*");

		Query searchQuery = new NativeSearchQueryBuilder()
				.withFilter(queryBuilder)
				.withPageable(PageRequest.of(0, 5))
				.build();

		SearchHits<Smartphone> searchSuggestions =
				elasticsearchOperations.search(searchQuery, Smartphone.class, IndexCoordinates.of(SMARTPHONES_INDEX));

		return searchSuggestions.stream()
				.map(SearchHit::getContent)
				.map(Smartphone.class::cast)
				.map(Smartphone::getName)
				.toList();
	}
}