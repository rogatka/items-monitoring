package com.items.parsing.service.parsers.games;

import com.items.parsing.client.RawgFeignClient;
import com.items.parsing.client.model.GamesResponse;
import com.items.parsing.service.messaging.kafka.KafkaNewItemsSender;
import com.items.parsing.service.parsers.ItemCategory;
import com.items.parsing.service.parsers.ItemsParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GamesParser implements ItemsParser {

    private static final int PAGE_SIZE = 100;
    private static final String METACRITIC_RATING_RANGE = "10,100";

    private final RawgFeignClient rawgFeignClient;

    private final WebClient webClient;

    private final KafkaNewItemsSender kafkaNewItemsSender;

    private final Clock clock;

    @Override
    public void parse() {
        LocalDate today = LocalDate.now(clock);
        LocalDate oneMonthAgoFromToday = today.minusMonths(1);
        int pageNumber = 1;
        
        getPageContent(pageNumber, oneMonthAgoFromToday, today)
                .expand(response -> {
                    if (response.getNext() == null) {
                        return Mono.empty();
                    }
                    return getPageContent(response.getNext());
                })
                .flatMapIterable(GamesResponse::getResults)
                .doOnNext(gameDto -> kafkaNewItemsSender.send(gameDto, UUID.randomUUID().toString(), ItemCategory.GAME))
                .subscribe();
    }

    private Mono<GamesResponse> getPageContent(int pageNumber, LocalDate from, LocalDate to) {
        String datesFormatted = String.format("%s,%s", DateTimeFormatter.ISO_LOCAL_DATE.format(from), DateTimeFormatter.ISO_LOCAL_DATE.format(to));
        return Mono.just(rawgFeignClient.getGames(pageNumber, PAGE_SIZE, METACRITIC_RATING_RANGE, datesFormatted, "-released"));
    }

    private Mono<GamesResponse> getPageContent(String url) {
        url = url.replace("%2C", ",");
        url = url.replace("%25", "%");
        return webClient
                .get()
                .uri(UriComponentsBuilder.fromHttpUrl(url).build().toUri())
                .retrieve()
                .bodyToMono(GamesResponse.class);
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.GAME;
    }
}
