package com.example.ITCanteen.controller;


import com.example.ITCanteen.dto.OrderRequest;
import com.example.ITCanteen.model.Order;
import com.example.ITCanteen.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class CanteenController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request){
        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{studentId}") //order story
    public ResponseEntity<List<Order>> getOrderStory(@PathVariable Long studentId){
        return ResponseEntity.ok(orderService.getOrderByStudentId(studentId));
    }

}
