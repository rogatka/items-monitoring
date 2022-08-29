package com.items.monitoring.service;

import com.items.monitoring.model.Item;
import com.items.monitoring.repository.ItemRepository;
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
public class ItemService {

    private final ItemRepository itemRepository;

    private final RedisCacheManager redisCacheManager;

    public Mono<Item> findById(String id) {
        Cache itemsCache = redisCacheManager.getCache("itemsCache");
        Objects.requireNonNull(itemsCache);
        Cache.ValueWrapper cachedValue = itemsCache.get(id);
        Mono<Item> itemMono;
        if (cachedValue == null) {
            itemMono = itemRepository.findById(id)
                    .doOnSuccess(item -> itemsCache.putIfAbsent(id, item))
                    .switchIfEmpty(Mono.error(() -> new RuntimeException(String.format("Item with id = %s not found", id))));
        } else {
            Item cachedItem = (Item) cachedValue.get();
            log.debug("Took cached value = {}", cachedItem);
            return Mono.just(cachedItem);
        }
        return itemMono;
    }

    public Flux<Item> findAll() {
        return itemRepository.findAll();
    }
}
