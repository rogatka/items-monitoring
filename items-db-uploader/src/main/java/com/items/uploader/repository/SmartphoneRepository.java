package com.items.uploader.repository;

import com.items.uploader.model.Smartphone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmartphoneRepository extends MongoRepository<Smartphone, String> {

    Optional<Smartphone> findFirstByCodeOrderByCreatedAtDesc(String code);
}
