package ru.taras_buriachenko.NauJava.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ru.taras_buriachenko.NauJava.dto.ReportData;
import ru.taras_buriachenko.NauJava.service.impl.ReportRenderer;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ThymeleafReportRenderer implements ReportRenderer {

    private static final String TEMPLATE_NAME = "report";

    private final SpringTemplateEngine templateEngine;

    @Override
    public String render(ReportData data) {
        Context context = new Context(Locale.forLanguageTag("ru"));
        context.setVariable("usersCount", data.usersCount());
        context.setVariable("usersElapsedMs", data.usersElapsedMs());
        context.setVariable("roomsElapsedMs", data.roomsElapsedMs());
        context.setVariable("totalElapsedMs", data.totalElapsedMs());
        context.setVariable("generatedAt", data.generatedAt());
        context.setVariable("rooms", data.rooms());

        return templateEngine.process(TEMPLATE_NAME, context);
    }
}