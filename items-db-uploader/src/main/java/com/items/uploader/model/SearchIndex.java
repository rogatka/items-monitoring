package com.items.uploader.model;

public enum SearchIndex {
    SMARTPHONES_INDEX("index_smartphones"),
    GAMES_INDEX("index_games");

    private final String indexName;

    SearchIndex(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexName() {
        return indexName;
    }
}
