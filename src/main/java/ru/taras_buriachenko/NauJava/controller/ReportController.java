package ru.taras_buriachenko.NauJava.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.taras_buriachenko.NauJava.dto.ReportCreatedResponseDto;
import ru.taras_buriachenko.NauJava.service.ReportService;
import ru.taras_buriachenko.NauJava.type.ReportStatus;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportCreatedResponseDto> createReport() {
        Long reportId = reportService.createReport();
        // CompletableFuture не дожидаемся - формирование идет в фоне
        reportService.generateReport(reportId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ReportCreatedResponseDto(reportId));
    }

    @GetMapping(value = "/{reportId}", produces = {MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> getReport(@PathVariable Long reportId) {
        ReportStatus status = reportService.getReportStatus(reportId);
        String content = reportService.getReportContent(reportId);

        MediaType mediaType = status == ReportStatus.COMPLETED
                ? MediaType.TEXT_HTML
                : MediaType.TEXT_PLAIN;

        return ResponseEntity.ok()
                .contentType(new MediaType(mediaType, StandardCharsets.UTF_8))
                .body(content);
    }
}