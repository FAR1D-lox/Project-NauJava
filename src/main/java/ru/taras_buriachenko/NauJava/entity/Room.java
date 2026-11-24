package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.taras_buriachenko.NauJava.type.RoomType;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "room")
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RoomType type;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}