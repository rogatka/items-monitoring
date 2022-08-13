package com.items.parsing.scheduler;


import com.items.parsing.parsers.CitilinkSmartphonesParser;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParsingScheduler {

    private final CitilinkSmartphonesParser citilinkSmartphonesParser;

    @Scheduled(cron = "${parsing.cron}")
    public void run() {
        citilinkSmartphonesParser.parse();
    }
}
