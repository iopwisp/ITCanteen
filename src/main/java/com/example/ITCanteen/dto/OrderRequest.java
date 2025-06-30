package com.example.ITCanteen.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotNull(message = "ID студента обязателен")
    private Long studentId;

    @NotEmpty(message = "Список блюд не может быть пустым")
    private List<OrderItemRequest> items;

    @Getter
    @Setter
    public static class OrderItemRequest {
        @NotNull(message = "ID блюда обязателен")
        private Long foodId;

        @Positive(message = "Количество должно быть больше 0")
        private int quantity;
    }
}