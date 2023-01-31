package com.items.monitoring.mapper;

import com.items.monitoring.configuration.MapperConfiguration;
import com.items.monitoring.model.Smartphone;
import com.items.monitoring.web.response.SmartphoneResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", config = MapperConfiguration.class)
public interface SmartphoneMapper {

    SmartphoneResponse map(Smartphone smartphone);

    List<SmartphoneResponse> map(List<Smartphone> smartphones);
}