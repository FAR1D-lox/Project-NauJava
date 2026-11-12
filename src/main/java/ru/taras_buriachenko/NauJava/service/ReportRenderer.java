package ru.taras_buriachenko.NauJava.service;

import ru.taras_buriachenko.NauJava.dto.ReportData;

public interface ReportRenderer {
    String render(ReportData data);
}