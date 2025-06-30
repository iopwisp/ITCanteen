package com.example.ITCanteen.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @PositiveOrZero(message = "Баланс не может быть отрицательным")
    @Column(name = "balance", nullable = false)
    private double balance;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}