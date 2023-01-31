package com.items.uploader.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.items.uploader.model.Smartphone;
import com.items.uploader.model.dto.SmartphoneDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static com.mongodb.assertions.Assertions.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SmartphoneMapperTest {

    private static final String ITEM_PATH = "smartphone.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final SmartphoneMapper smartphoneMapper = Mappers.getMapper(SmartphoneMapper.class);

    @Test
    void shouldCorrectlyMapNullItem() {
        assertNull(smartphoneMapper.map(null));
    }

    @Test
    void shouldCorrectlyMapItemWithNullValues() {
        SmartphoneDto smartphoneDto = new SmartphoneDto();
        smartphoneDto.setName("test");

        assertThat(smartphoneMapper.map(smartphoneDto))
                .hasFieldOrPropertyWithValue("name", smartphoneDto.getName())
                .hasAllNullFieldsOrPropertiesExcept("name");
    }

    @Test
    void shouldCorrectlyMapItem() throws JsonProcessingException {
        String itemJson = readFileContent(ITEM_PATH);
        SmartphoneDto smartphoneDto = objectMapper.readValue(itemJson, SmartphoneDto.class);

        assertThat(smartphoneMapper.map(smartphoneDto))
                .hasFieldOrPropertyWithValue("name", "Смартфон Vertex Impress Luck 8Gb,  черный")
                .hasFieldOrPropertyWithValue("lastPrice", BigDecimal.valueOf(2690))
                .hasFieldOrPropertyWithValue("code", "1107892")
                .hasFieldOrPropertyWithValue("operatingSystem", "Android 7.0")
                .hasFieldOrPropertyWithValue("cpu", "MediaTek MT6580, 1300МГц, 4-х ядерный")
                .hasFieldOrPropertyWithValue("storageSize", "8 ГБ")
                .hasFieldOrPropertyWithValue("ramSize", "1 ГБ")
                .extracting(Smartphone::getDisplayInfo)
                .hasFieldOrPropertyWithValue("type", "5\", IPS")
                .hasFieldOrPropertyWithValue("resolution", "854x480");
    }

    @SneakyThrows
    static String readFileContent(String path) {
        URL url = SmartphoneMapperTest.class.getClassLoader().getResource(path);
        Objects.requireNonNull(url);
        return Files.readString(Paths.get(url.toURI()));
    }
}