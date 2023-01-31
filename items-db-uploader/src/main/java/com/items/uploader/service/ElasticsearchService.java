package com.items.uploader.service;

import com.items.uploader.model.Item;
import com.items.uploader.model.SearchIndex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public String indexItem(Item item, SearchIndex searchIndex) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(item.getId())
                .withObject(item)
                .build();
        return elasticsearchOperations.index(indexQuery, IndexCoordinates.of(searchIndex.getIndexName()));
    }
}