package com.items.monitoring.web;

import com.items.monitoring.mapper.SmartphoneMapper;
import com.items.monitoring.service.SmartphoneService;
import com.items.monitoring.web.response.SmartphoneResponse;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Observed
@Tag(name = "Smartphones API")
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/smartphones")
@RequiredArgsConstructor
public class SmartphoneController {

    private final SmartphoneService smartphoneService;

    private final SmartphoneMapper smartphoneMapper;

    @Operation(description = "Find smartphone by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<SmartphoneResponse> findById(@PathVariable String id) {
        return smartphoneService.findById(id).map(smartphoneMapper::map);
    }

    @Operation(description = "Find all smartphones")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<SmartphoneResponse> findAll() {
        return smartphoneService.findAll().map(smartphoneMapper::map);
    }
}
