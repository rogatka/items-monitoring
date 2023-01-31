package com.items.uploader.mapper;

import com.items.uploader.configuration.MapperConfiguration;
import com.items.uploader.model.Smartphone;
import com.items.uploader.model.dto.SmartphoneDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface SmartphoneMapper {

    @Mapping(source = "price", target = "lastPrice")
    Smartphone map(SmartphoneDto dto);
}