package com.example.ITCanteen.controller;

import com.example.ITCanteen.model.Category;
import com.example.ITCanteen.model.Food;
import com.example.ITCanteen.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<Food> addFood(@Valid @RequestBody Food food) {
        return ResponseEntity.ok(foodService.addFood(food));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Блюдо не найдено с ID: " + id)));
    }

    @GetMapping
    public ResponseEntity<List<Food>> getAllFoods() {
        return ResponseEntity.ok(foodService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFood(@PathVariable Long id, @Valid @RequestBody Food food) {
        return ResponseEntity.ok(foodService.updateFood(id, food));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Food>> getFoodsByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(foodService.findByCategoryId(categoryId));
    }
}