package com.items.monitoring.repository.mongo;

import com.items.monitoring.model.Smartphone;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SmartphoneRepository extends ReactiveMongoRepository<Smartphone, String> {

    Flux<Smartphone> findByNameLike(String query);
}
