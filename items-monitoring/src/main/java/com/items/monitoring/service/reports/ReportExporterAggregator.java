package com.items.monitoring.service.reports;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Slf4j
@Component
public class ReportExporterAggregator {

    private final Map<ReportCase, ReportExporter> reportExportersMap;

    public ReportExporterAggregator(List<ReportExporter> reportExporters) {
        this.reportExportersMap = reportExporters.stream()
                .collect(Collectors.toMap(ReportExporter::getReportCase, identity()));
    }

    public ReportExporter getExporter(ReportCase reportCase) {
        return Optional.ofNullable(reportExportersMap.get(reportCase))
                .orElseThrow(() ->
                        new RuntimeException(String.format("No ReportExporter found for case [%s]", reportCase))
                );
    }
}
