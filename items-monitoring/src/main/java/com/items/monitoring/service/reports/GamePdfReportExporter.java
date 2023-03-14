package com.items.monitoring.service.reports;

import com.items.monitoring.model.Game;
import com.items.monitoring.model.ItemCategory;
import com.items.monitoring.model.Report;
import com.items.monitoring.repository.mongo.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class GamePdfReportExporter implements ReportExporter {

    private static final String GAME_REPORT_FILE_NAME = "games_rating";

    private final GameRepository gameRepository;
    private final Clock clock;

    @SneakyThrows
    public Report export() {
        try (InputStream employeeReportStream = getClass().getResourceAsStream("/reports/games_rating.jrxml");
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
            LocalDate oneMonthAgo = LocalDate.now(clock).minusMonths(1);

            // Flux<Game> result = gameRepository.findByReleaseDateAfter(
            // oneMonthAgo.format(DateTimeFormatter.ISO_LOCAL_DATE), Sort.by(Sort.Direction.DESC, "lastRating")
            // );
            Flux<Game> result = gameRepository.findByReleaseDateAfter(
                    oneMonthAgo, Sort.by(Sort.Direction.DESC, "lastRating")
            );
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(result
                    .collectList()
                    .toFuture()
                    .get());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new Report(GAME_REPORT_FILE_NAME + getReportCase().getFileExtension(), outputStream.toByteArray());
        }
    }

    @Override
    public ReportCase getReportCase() {
        return ReportCase.of(ItemCategory.GAME, ".pdf");
    }
}
