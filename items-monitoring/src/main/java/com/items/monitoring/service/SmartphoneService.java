package com.items.monitoring.service;

import com.items.monitoring.model.Smartphone;
import com.items.monitoring.repository.mongo.SmartphoneRepository;
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
public class SmartphoneService {

    private final SmartphoneRepository smartphoneRepository;

    private final RedisCacheManager redisCacheManager;

    public Mono<Smartphone> findById(String id) {
        Cache smartphonesCache = redisCacheManager.getCache("smartphonesCache");
        Objects.requireNonNull(smartphonesCache);
        Cache.ValueWrapper cachedValue = smartphonesCache.get(id);
        Mono<Smartphone> smartphoneMono;
        if (cachedValue == null) {
            smartphoneMono = smartphoneRepository.findById(id)
                    .doOnSuccess(smartphone -> smartphonesCache.putIfAbsent(id, smartphone))
                    .switchIfEmpty(Mono.error(() ->
                            new RuntimeException(String.format("Smartphone with id = %s not found", id)))
                    );
        } else {
            Smartphone cachedSmartphone = (Smartphone) cachedValue.get();
            log.debug("Took cached value = {}", cachedSmartphone);
            return Mono.just(cachedSmartphone);
        }
        return smartphoneMono;
    }

    public Flux<Smartphone> findAll() {
        return smartphoneRepository.findAll();
    }
}
