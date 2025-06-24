package com.example.ITCanteen.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    private Long studentId;
    private List<Long> foodId;
}
