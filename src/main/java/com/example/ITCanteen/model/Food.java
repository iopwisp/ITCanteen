package com.example.ITCanteen.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "food")
public class Food {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private int price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}