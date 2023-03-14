package com.items.monitoring.service.reports;

import com.items.monitoring.model.Game;
import com.items.monitoring.model.Item;
import com.items.monitoring.model.ItemCategory;
import com.items.monitoring.model.Report;
import com.items.monitoring.repository.mongo.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameXlsxReportExporter implements ReportExporter {

    private final GameRepository gameRepository;
    private final Clock clock;

    @SneakyThrows
    public Report export() {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("games_rating");

            List<String> headers = List.of("Game", "Game Added", "Rating last changed", "Release Date", "Rating");
            fillHeaders(sheet, workbook, headers);

            LocalDate oneMonthAgo = LocalDate.now(clock).minusMonths(1);

            List<Game> games = gameRepository.findByReleaseDateAfter(
                            oneMonthAgo.format(DateTimeFormatter.ISO_LOCAL_DATE),
                            Sort.by(Sort.Direction.DESC, "lastRating")
                    )
                    .collectList()
                    .toFuture()
                    .get();

            fillRows(sheet, workbook, games);
            autoSizeColumns(sheet, 0, headers.size());
            workbook.write(outputStream);
            return new Report("games_rating" + getReportCase().getFileExtension(), outputStream.toByteArray());
        }
    }

    private static void fillHeaders(Sheet sheet, Workbook workbook, List<String> headers) {
        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell;
        for (int i = 0; i < headers.size(); i++) {
            headerCell = header.createCell(i);
            headerCell.setCellValue(headers.get(i));
            headerCell.setCellStyle(headerStyle);
        }
    }

    private static void fillRows(Sheet sheet, Workbook workbook, List<Game> games) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat((short) 14);
        dateCellStyle.setWrapText(true);

        for (int i = 0; i < games.size(); i++) {
            Row row = sheet.createRow(i + 2);

            Item game = games.get(i);

            Cell cell = row.createCell(0);
            cell.setCellValue(((Game) game).getName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(Date.from(game.getCreatedAt()));
            cell.setCellStyle(dateCellStyle);

            cell = row.createCell(2);
            cell.setCellValue(Date.from(game.getUpdatedAt()));
            cell.setCellStyle(dateCellStyle);

            cell = row.createCell(3);
            cell.setCellValue(((Game) game).getReleaseDate());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellValue(((Game) game).getLastRating());
            cell.setCellStyle(style);
        }
    }

    private void autoSizeColumns(Sheet sheet, int firstCol, int lastCol) {
        for (int col = firstCol; col <= lastCol; col++) {
            sheet.autoSizeColumn(col, true);
        }
    }

    @Override
    public ReportCase getReportCase() {
        return ReportCase.of(ItemCategory.GAME, ".xlsx");
    }
}
