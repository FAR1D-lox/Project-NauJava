package ru.taras_buriachenko.NauJava.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.taras_buriachenko.NauJava.dto.ReportData;
import ru.taras_buriachenko.NauJava.entity.Report;
import ru.taras_buriachenko.NauJava.entity.Room;
import ru.taras_buriachenko.NauJava.exception.ResourceNotFoundException;
import ru.taras_buriachenko.NauJava.repository.crud.ReportRepository;
import ru.taras_buriachenko.NauJava.repository.crud.RoomRepository;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.service.ReportService;
import ru.taras_buriachenko.NauJava.type.ReportStatus;
import ru.taras_buriachenko.NauJava.service.ReportRenderer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PlatformTransactionManager transactionManager;
    private final ReportRenderer reportRenderer;


    @Transactional
    @Override
    public Long createReport() {
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);
        reportRepository.save(report);
        return report.getId();
    }

    @Override
    public String getReportContent(Long reportId) {
        Report report = findReport(reportId);
        return switch (report.getStatus()) {
            case CREATED -> "Отчет с id = " + reportId + " еще не сформирован. "
                    + "Статус отчета: создан. Попробуйте запросить отчет позже.";
            case ERROR -> "Отчет с id = " + reportId + " сформирован с ошибкой. "
                    + "Статус отчета: ошибка. Причина: "
                    + (report.getContent() == null ? "неизвестна" : report.getContent());
            case COMPLETED -> report.getContent();
        };
    }

    @Override
    public ReportStatus getReportStatus(Long reportId) {
        return findReport(reportId).getStatus();
    }

    @Override
    public CompletableFuture<Long> generateReport(Long reportId) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            AtomicLong usersCount = new AtomicLong(0L);
            AtomicLong usersElapsed = new AtomicLong(0L);

            AtomicReference<List<Room>> roomsRef = new AtomicReference<>(List.of());
            AtomicLong roomsElapsed = new AtomicLong(0L);

            AtomicReference<Throwable> failure = new AtomicReference<>();

            // Поток №1 - количество зарегистрированных пользователей
            Thread usersThread = new Thread(() -> {
                long threadStartTime = System.currentTimeMillis();
                try {
                    usersCount.set(userRepository.count());
                } catch (Throwable e) {
                    failure.compareAndSet(null, e);
                } finally {
                    usersElapsed.set(System.currentTimeMillis() - threadStartTime);
                }
            }, "report-users-" + reportId);

            // Поток №2 - список комнат
            Thread roomsThread = new Thread(() -> {
                long threadStartTime = System.currentTimeMillis();
                try {
                    List<Room> rooms = new ArrayList<>();
                    roomRepository.findAll().forEach(rooms::add);
                    roomsRef.set(rooms);
                } catch (Throwable e) {
                    failure.compareAndSet(null, e);
                } finally {
                    roomsElapsed.set(System.currentTimeMillis() - threadStartTime);
                }
            }, "report-rooms-" + reportId);

            usersThread.start();
            roomsThread.start();

            // Ждет завершения обоих потоков
            try {
                usersThread.join();
                roomsThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                failure.compareAndSet(null, e);
            }

            String content;
            ReportStatus resultStatus;
            try {
                if (failure.get() != null) {
                    throw new IllegalStateException(failure.get().getMessage(), failure.get());
                }
                // Время построения HTML входит в общее время отчета
                long totalElapsed = System.currentTimeMillis() - startTime;
                content = reportRenderer.render(toReportData(
                        usersCount.get(), roomsRef.get(),
                        usersElapsed.get(), roomsElapsed.get(), totalElapsed));
                resultStatus = ReportStatus.COMPLETED;
            } catch (Exception e) {
                content = "Ошибка формирования отчета: " + e.getMessage();
                resultStatus = ReportStatus.ERROR;
            }

            updateReport(reportId, resultStatus, content);
            return reportId;
        });
    }

    private Report findReport(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Report with id = " + reportId + " not found"));
    }

    private void updateReport(Long reportId, ReportStatus status, String content) {
        //Тут оставлю manager. Не хочу лишний раз выделять сервис или прокидывать сервис в самого себя через Lazy.
        TransactionStatus transactionStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Report report = findReport(reportId);
            report.setStatus(status);
            report.setContent(content);
            reportRepository.save(report);
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            throw e;
        }
    }

        private ReportData toReportData(long usersCount,
                                        List<Room> rooms,
                                        long usersElapsed,
                                        long roomsElapsed,
                                        long totalElapsed) {
        List<ReportData.RoomRow> rows = rooms.stream()
                .map(room -> new ReportData.RoomRow(
                        room.getId(),
                        room.getTitle(),
                        room.getType() == null ? null : room.getType().name(),
                        room.getCreatedAt() == null
                                ? null : room.getCreatedAt().format(DATE_TIME_FORMATTER)))
                .toList();

        return new ReportData(
                usersCount,
                usersElapsed,
                roomsElapsed,
                totalElapsed,
                LocalDateTime.now().format(DATE_TIME_FORMATTER),
                rows);
    }

}