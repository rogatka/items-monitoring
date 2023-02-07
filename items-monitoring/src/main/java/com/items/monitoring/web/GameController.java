package com.items.monitoring.web;

import com.items.monitoring.mapper.GameMapper;
import com.items.monitoring.service.GameService;
import com.items.monitoring.web.response.GameResponse;
import io.micrometer.observation.annotation.Observed;
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

@Observed
@Tag(name = "Games API")
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    private final GameMapper gameMapper;

    @Operation(description = "Find game by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<GameResponse> findById(@PathVariable String id) {
        return gameService.findById(id).map(gameMapper::map);
    }

    @Operation(description = "Find all games")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<GameResponse> findAll() {
        return gameService.findAll().map(gameMapper::map);
    }
}