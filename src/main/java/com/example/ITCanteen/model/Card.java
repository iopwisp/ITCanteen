package com.example.ITCanteen.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "card")
public class Card {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "balance", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private int balance;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "student_id")
    private Student student;

}