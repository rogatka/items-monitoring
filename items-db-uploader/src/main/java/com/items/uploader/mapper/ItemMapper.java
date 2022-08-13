package com.items.uploader.mapper;

import com.items.uploader.configuration.MapperConfiguration;
import com.items.uploader.model.Item;
import com.items.uploader.model.dto.ItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface ItemMapper {

    @Mapping(source = "price", target = "lastPrice")
    Item map(ItemDto dto);
}