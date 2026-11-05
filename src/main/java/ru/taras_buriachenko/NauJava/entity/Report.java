package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.*;
import ru.taras_buriachenko.NauJava.type.ReportStatus;

@Entity
@Table(name = "report")
public class Report {

    public Report() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32,
            columnDefinition = "varchar(32) default 'CREATED'")
    private ReportStatus status = ReportStatus.CREATED;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}