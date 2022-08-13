package com.items.parsing.parsers;

import com.items.parsing.configuration.properties.CitilinkParsingProperties;
import com.items.parsing.messaging.kafka.KafkaNewItemsSender;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CitilinkSmartphonesParser implements ItemsParser {

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

    private final CitilinkParsingProperties citilinkParsingProperties;

    private final WebClient webClient;

    private final KafkaNewItemsSender kafkaNewItemsSender;

    @Override
    public void parse() {
        int pageNumber = 1;
        boolean shouldParse = true;
        while (shouldParse) {
            Document doc = Jsoup.parse(getResponse(pageNumber));
            Elements items = doc.getElementsByAttributeValue("class", "ProductCardHorizontal__header-block");
            if (items.isEmpty()) {
                shouldParse = false;
            }
            Elements prices = doc.getElementsByAttributeValue("class", "ProductPrice ProductPrice_default ProductPrice_size_m ProductCardHorizontal__price");
            for (int i = 0; i < items.size(); i++) {
                Element aElement = items.get(i).select("a").first();
                String title = aElement.attr("title");
                String itemCode = items.get(i).getElementsByAttributeValue("class", "ProductCardHorizontal__vendor-code")
                        .first()
                        .text(); // Код товара: 1107885
                String itemCodeFormatted = itemCode.replace("Код товара:", "").trim();

                Element itemProperties = doc.getElementsByAttributeValue("class", "ProductCardHorizontal__properties").first();
                Elements liElements = itemProperties.select("li");


                Map<String, String> propertiesMap = PROPERTIES.stream()
                        .collect(Collectors.toMap(Function.identity(), property -> {
                            for (Element element : liElements) {
                                if (element.text().contains(property)) {
                                    return element.getElementsByAttributeValue("class", "ProductCardHorizontal__properties_value")
                                            .first()
                                            .text();
                                }
                            }
                            return null;
                        }));

                ItemDto itemDto = ItemDto.builder()
                        .name(title)
                        .code(itemCodeFormatted)
                        .operatingSystem(propertiesMap.get(OPERATING_SYSTEM))
                        .displayInfo(ItemDto.DisplayInfo.builder()
                                .type(propertiesMap.get(DISPLAY))
                                .resolution(propertiesMap.get(DISPLAY_RESOLUTION))
                                .build())
                        .cpu(propertiesMap.get(CPU))
                        .ramSize(propertiesMap.get(RAM_SIZE))
                        .storageSize(propertiesMap.get(STORAGE_SIZE))
                        .build();
                String priceString = prices.get(i).text();
                itemDto.setPrice(new BigDecimal(priceString.substring(0, priceString.length() - 2).replace(" ", "")));
                kafkaNewItemsSender.send(itemDto, UUID.randomUUID().toString());
            }
            pageNumber++;
        }

    }

    private String getResponse(int pageNumber) {
        return webClient
                .get()
                .uri(citilinkParsingProperties.getBaseUrl() + "/catalog/smartfony/?sorting=price_asc" + "&p=" + pageNumber)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(String.class);
                    }
                    return Mono.just("");
                })
                .block();
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.SMARTPHONE;
    }
}
