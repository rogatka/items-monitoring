package com.items.uploader.mapper;

import com.items.uploader.configuration.MapperConfiguration;
import com.items.uploader.model.Game;
import com.items.uploader.model.dto.GameDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface GameMapper {

    @Mapping(source = "metacritic", target = "lastRating")
    @Mapping(source = "id", target = "platformId")
    @Mapping(source = "released", target = "releaseDate")
    Game map(GameDto dto);
}