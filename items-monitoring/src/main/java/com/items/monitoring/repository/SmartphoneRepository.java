package com.items.monitoring.repository;

import com.items.monitoring.model.Smartphone;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmartphoneRepository extends ReactiveMongoRepository<Smartphone, String> {

}
