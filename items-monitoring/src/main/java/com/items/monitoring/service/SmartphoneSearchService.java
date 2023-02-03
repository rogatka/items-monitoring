package com.items.monitoring.service;

import com.items.monitoring.model.Smartphone;
import com.items.monitoring.repository.elasticsearch.ElasticsearchSmartphoneRepository;
import com.items.monitoring.repository.mongo.SmartphoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Slf4j
@Service
@RequiredArgsConstructor
public class SmartphoneSearchService {

    private static final int PAGE_SIZE = 5;
    private static final int PAGE_NUMBER = 0;

    private final ElasticsearchSmartphoneRepository elasticsearchSmartphoneRepository;
    private final SmartphoneRepository smartphoneRepository;

    public Flux<Smartphone> processSearch(final String query) {
        log.info("Search with query {}", query);
        // search in elasticsearch
        return elasticsearchSmartphoneRepository.findFuzzyByName(query)
                // if empty, go to mongo
                .switchIfEmpty(s -> smartphoneRepository.findByNameLike(query)
                        // if found, save to elasticsearch so next time value will be retrieved from elasticsearch
                        .doOnNext(e -> elasticsearchSmartphoneRepository.save(e).subscribe())
                        .subscribe(s));
    }

    public Flux<String> fetchSuggestions(String query) {
        return elasticsearchSmartphoneRepository.findByNameLike(query, PageRequest.of(PAGE_NUMBER, PAGE_SIZE))
                .map(Smartphone::getName);
    }
}