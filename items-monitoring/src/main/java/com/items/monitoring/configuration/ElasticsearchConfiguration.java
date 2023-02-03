package com.items.monitoring.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

@Configuration
@EnableReactiveElasticsearchRepositories(basePackages = "com.items.monitoring.repository.elasticsearch")
public class ElasticsearchConfiguration {

}
