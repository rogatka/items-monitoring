package com.items.parsing.runner;

import com.items.parsing.parsers.ItemsParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParsingRunner implements ApplicationRunner {

    private final List<ItemsParser> parsers;

    @Override
    public void run(ApplicationArguments args) {
        parsers.forEach(itemsParser -> {
            try {
                itemsParser.parse();
            } catch (Exception e) {
                log.error(String.format("Error while parsing items by %s parser. Error message: %s", itemsParser.getCategory().name(), e.getMessage()));
            }
        });
    }
}
