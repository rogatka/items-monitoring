package com.items.monitoring.mapper;

import com.items.monitoring.configuration.MapperConfiguration;
import com.items.monitoring.model.Game;
import com.items.monitoring.web.response.GameResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface GameMapper {

    GameResponse map(Game game);

    List<GameResponse> map(List<Game> games);
}