package com.items.monitoring.service;

import com.items.monitoring.model.Game;
import com.items.monitoring.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    private final RedisCacheManager redisCacheManager;

    public Mono<Game> findById(String id) {
        Cache gamesCache = redisCacheManager.getCache("gamesCache");
        Objects.requireNonNull(gamesCache);
        Cache.ValueWrapper cachedValue = gamesCache.get(id);
        Mono<Game> gameMono;
        if (cachedValue == null) {
            gameMono = gameRepository.findById(id)
                    .doOnSuccess(smartphone -> gamesCache.putIfAbsent(id, smartphone))
                    .switchIfEmpty(Mono.error(() -> new RuntimeException(String.format("Smartphone with id = %s not found", id))));
        } else {
            Game cachedGame = (Game) cachedValue.get();
            log.debug("Took cached value = {}", cachedGame);
            return Mono.just(cachedGame);
        }
        return gameMono;
    }

    public Flux<Game> findAll() {
        return gameRepository.findAll();
    }
}
