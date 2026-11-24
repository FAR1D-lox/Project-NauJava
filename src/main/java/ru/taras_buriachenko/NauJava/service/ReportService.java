package ru.taras_buriachenko.NauJava.service;

import ru.taras_buriachenko.NauJava.type.ReportStatus;

import java.util.concurrent.CompletableFuture;

public interface ReportService {

    Long createReport();

    CompletableFuture<Long> generateReport(Long reportId);

    String getReportContent(Long reportId);

    ReportStatus getReportStatus(Long reportId);
}