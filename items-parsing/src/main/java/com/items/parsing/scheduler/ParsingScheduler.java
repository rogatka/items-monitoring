package com.items.parsing.scheduler;


import com.items.parsing.parsers.ItemsParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParsingScheduler {

    private final List<ItemsParser> parsers;

    @Scheduled(cron = "${parsing.cron}")
    public void run() {
        parsers.forEach(itemsParser -> {
            try {
                itemsParser.parse();
            } catch (Exception e) {
                log.error(String.format("Error while parsing items by %s parser. Error message: %s", itemsParser.getCategory().name(), e.getMessage()));
            }
        });
    }
}
