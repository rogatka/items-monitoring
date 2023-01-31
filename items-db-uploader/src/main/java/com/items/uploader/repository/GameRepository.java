package com.items.uploader.repository;

import com.items.uploader.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {

    Optional<Game> findFirstByPlatformIdOrderByCreatedAtDesc(Long platformId);
}
