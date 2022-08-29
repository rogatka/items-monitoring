package com.items.monitoring.web;

import com.items.monitoring.mapper.ItemMapper;
import com.items.monitoring.service.ItemService;
import com.items.monitoring.web.response.ItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Items API")
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    @Operation(description = "Find item by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ItemResponse> findById(@PathVariable String id) {
        return itemService.findById(id).map(itemMapper::map);
    }

    @Operation(description = "Find all items")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<ItemResponse> findAll() {
        return itemService.findAll().map(itemMapper::map);
    }
}
