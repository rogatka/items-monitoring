package com.items.monitoring.mapper;

import com.items.monitoring.configuration.MapperConfiguration;
import com.items.monitoring.model.Item;
import com.items.monitoring.web.response.ItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface ItemMapper {

    ItemResponse map(Item item);

    List<ItemResponse> map(List<Item> items);
}