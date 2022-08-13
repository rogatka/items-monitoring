package com.items.parsing.runner;

import com.items.parsing.parsers.CitilinkSmartphonesParser;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParsingRunner implements ApplicationRunner {

    private final CitilinkSmartphonesParser citilinkSmartphonesParser;

    @Override
    public void run(ApplicationArguments args) {
        citilinkSmartphonesParser.parse();
    }
}
