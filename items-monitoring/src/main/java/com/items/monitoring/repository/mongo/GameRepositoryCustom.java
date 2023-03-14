package com.items.monitoring.repository.mongo;

import com.items.monitoring.model.Game;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface GameRepositoryCustom {

    Flux<Game> findByReleaseDateAfter(LocalDate date, Sort sort);
}
