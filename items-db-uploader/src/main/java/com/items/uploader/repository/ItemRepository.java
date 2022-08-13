package com.items.uploader.repository;

import com.items.uploader.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

    Optional<Item> findFirstByCodeOrderByCreatedAtDesc(String code);

    @Query("{'price': ?0 }")
    List<Item> findAllByPrice(BigDecimal price);
}
