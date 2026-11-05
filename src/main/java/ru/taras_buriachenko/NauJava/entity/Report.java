package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.taras_buriachenko.NauJava.type.ReportStatus;

@Setter
@Getter
@Entity
@Table(name = "report")
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32,
            columnDefinition = "varchar(32) default 'CREATED'")
    private ReportStatus status = ReportStatus.CREATED;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}