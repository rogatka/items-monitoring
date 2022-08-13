package com.items.monitoring.service;

import com.items.monitoring.model.Item;
import com.items.monitoring.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item findById(String id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Item with id = %s not found", id)));
    }

    @Cacheable(value = "itemsCache")
    public List<Item> findAll() {
        log.debug("Getting all items...");
        return itemRepository.findAll();
    }
}
