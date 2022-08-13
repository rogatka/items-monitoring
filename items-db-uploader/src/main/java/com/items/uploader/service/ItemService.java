package com.items.uploader.service;

import com.items.uploader.mapper.ItemMapper;
import com.items.uploader.model.Item;
import com.items.uploader.model.dto.ItemDto;
import com.items.uploader.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    public Optional<Item> findFirstByCodeOrderByCreatedAtDesc(String code) {
        return itemRepository.findFirstByCodeOrderByCreatedAtDesc(code);
    }


    @Transactional
    public Item save(Item item) {
        log.debug("Saving item {}", item);
        return itemRepository.save(item);
    }

    @Transactional
    public Item save(ItemDto itemDto) {
        Item mappedItem = itemMapper.map(itemDto);
        log.debug("Saving item {}", mappedItem);
        return itemRepository.save(mappedItem);
    }
}
