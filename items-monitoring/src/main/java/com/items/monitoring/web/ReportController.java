package com.items.monitoring.web;

import com.items.monitoring.model.ItemCategory;
import com.items.monitoring.model.Report;
import com.items.monitoring.service.reports.ReportCase;
import com.items.monitoring.service.reports.ReportExporterAggregator;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Observed
@Tag(name = "Reports API")
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportExporterAggregator reportExporterAggregator;

    @Operation(description = "Generate PDF report")
    @GetMapping("/games/pdf")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Object>> generatePDFReport() {
        Report report = reportExporterAggregator.getExporter(ReportCase.of(ItemCategory.GAME, ".pdf")).export();
        return Mono.just(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + report.getFileName())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-download")
                .body(new ByteArrayResource(report.getContent())));
    }

    @Operation(description = "Generate XLSX report")
    @GetMapping("/games/xlsx")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Object>> generateXLSXReport() {
        Report report = reportExporterAggregator.getExporter(ReportCase.of(ItemCategory.GAME, ".xlsx")).export();
        return Mono.just(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + report.getFileName())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-download")
                .body(new ByteArrayResource(report.getContent())));
    }
}