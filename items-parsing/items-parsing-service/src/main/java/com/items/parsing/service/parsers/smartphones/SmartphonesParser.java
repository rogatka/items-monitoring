package com.items.parsing.service.parsers.smartphones;

import com.items.parsing.service.configuration.properties.CitilinkParsingProperties;
import com.items.parsing.service.kafka.KafkaNewItemsSender;
import com.items.parsing.service.model.SmartphoneDto;
import com.items.parsing.service.parsers.ItemCategory;
import com.items.parsing.service.parsers.ItemsParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmartphonesParser implements ItemsParser {

    private static final String OPERATING_SYSTEM = "Операционная система";
    private static final String DISPLAY = "Дисплей";
    private static final String DISPLAY_RESOLUTION = "Разрешение дисплея";
    private static final String CPU = "Процессор";
    private static final String RAM_SIZE = "Объем оперативной памяти";
    private static final String STORAGE_SIZE = "Объем встроенной памяти";

    private static final List<String> PROPERTIES = List.of(
            OPERATING_SYSTEM,
            DISPLAY,
            DISPLAY_RESOLUTION,
            CPU,
            RAM_SIZE,
            STORAGE_SIZE
    );

    private static final String HTML_ATTRIBUTE_CLASS = "class";

    private final CitilinkParsingProperties citilinkParsingProperties;

    private final WebClient webClient;

    private final KafkaNewItemsSender kafkaNewItemsSender;

    @Override
    public void parse() {
        int pageNumber = 1;
        boolean shouldParse = true;
        while (shouldParse) {
            String smartphonesPageContent = getPageContent(pageNumber);
            Document doc = Jsoup.parse(smartphonesPageContent);
            // Items are placed under the "ProductCardHorizontal__header-block" attribute
            Elements items = doc.getElementsByAttributeValue(HTML_ATTRIBUTE_CLASS, "ProductCardHorizontal__header-block");
            if (items.isEmpty()) {
                // All items have been processed
                shouldParse = false;
            }

            // Prices are placed under the separate "ProductPrice ProductPrice_default ProductPrice_size_m ProductCardHorizontal__price" attribute
            Elements prices = doc.getElementsByAttributeValue(HTML_ATTRIBUTE_CLASS, "ProductPrice ProductPrice_default ProductPrice_size_m ProductCardHorizontal__price");
            if (prices.size() != items.size()) {
                log.error("Number of items' prices({}) is not equal to the number of items({})", prices.size(), items.size());
                return;
            }

            for (int i = 0; i < items.size(); i++) {
                Element aElement = items.get(i).select("a").first();
                String title = Optional.ofNullable(aElement)
                        .map(elem -> elem.attr("title"))
                        .orElse(null);
                String itemCodeString = Optional.ofNullable(items.get(i).getElementsByAttributeValue(HTML_ATTRIBUTE_CLASS, "ProductCardHorizontal__vendor-code")
                                .first())
                        .map(Element::text)
                        .orElse(null);
                String itemCodeFormatted = Optional.ofNullable(itemCodeString)
                        .map(itemCode -> itemCode.replace("Код товара:", "").trim())
                        .orElse(null);

                Optional<Elements> liElementsOptional = Optional.ofNullable(doc.getElementsByAttributeValue(HTML_ATTRIBUTE_CLASS, "ProductCardHorizontal__properties")
                                .first())
                        .map(itemProperties -> itemProperties.select("li"));

                Map<String, String> propertiesMap = liElementsOptional
                        .map(this::buildItemPropertiesMap)
                        .orElseGet(HashMap::new);

                String priceString = prices.get(i).text();
                SmartphoneDto smartphoneDto = SmartphoneDto.builder()
                        .name(title)
                        .code(itemCodeFormatted)
                        .operatingSystem(propertiesMap.get(OPERATING_SYSTEM))
                        .displayInfo(SmartphoneDto.DisplayInfo.builder()
                                .type(propertiesMap.get(DISPLAY))
                                .resolution(propertiesMap.get(DISPLAY_RESOLUTION))
                                .build())
                        .cpu(propertiesMap.get(CPU))
                        .ramSize(propertiesMap.get(RAM_SIZE))
                        .storageSize(propertiesMap.get(STORAGE_SIZE))
                        .price(new BigDecimal(priceString.substring(0, priceString.length() - 2).replace(" ", "")))
                        .build();
                kafkaNewItemsSender.send(smartphoneDto, UUID.randomUUID().toString(), ItemCategory.SMARTPHONE);
            }
            pageNumber++;
        }

    }

    private String getPageContent(int pageNumber) {
        return webClient
                .get()
                .uri(UriComponentsBuilder.fromHttpUrl(citilinkParsingProperties.getBaseUrl())
                        .path("/catalog/smartfony")
                        .queryParam("sorting", "price_asc")
                        .queryParam("p", pageNumber)
                        .build()
                        .toUri())
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(String.class);
                    }
                    return Mono.just("");
                })
                .block();
    }

    private Map<String, String> buildItemPropertiesMap(Elements liElements) {
        Map<String, String> propertiesMap = new HashMap<>();
        PROPERTIES.forEach(property -> {
            String propertyValue = null;
            for (Element element : liElements) {
                if (element.text().contains(property)) {
                    propertyValue = Optional.ofNullable(element.getElementsByAttributeValue(HTML_ATTRIBUTE_CLASS, "ProductCardHorizontal__properties_value")
                                    .first())
                            .map(Element::text)
                            .orElse(null);
                    break;
                }
            }
            propertiesMap.put(property, propertyValue);
        });
        return propertiesMap;
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.SMARTPHONE;
    }
}
