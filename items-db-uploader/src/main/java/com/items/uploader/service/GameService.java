package com.items.uploader.service;

import com.items.uploader.mapper.GameMapper;
import com.items.uploader.model.Game;
import com.items.uploader.model.dto.GameDto;
import com.items.uploader.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    private final GameMapper gameMapper;

    public Optional<Game> findFirstByPlatformIdOrderByCreatedAtDesc(Long platformId) {
        return gameRepository.findFirstByPlatformIdOrderByCreatedAtDesc(platformId);
    }

    @Transactional
    public Game save(Game game) {
        log.debug("Saving item {}", game);
        return gameRepository.save(game);
    }

    @Transactional
    public Game save(GameDto gameDto) {
        Game mappedGame = gameMapper.map(gameDto);
        log.debug("Saving item {}", mappedGame);
        return gameRepository.save(mappedGame);
    }
}
