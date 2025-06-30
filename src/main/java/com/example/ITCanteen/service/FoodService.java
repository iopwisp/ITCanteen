package com.example.ITCanteen.service;

import com.example.ITCanteen.model.Category;
import com.example.ITCanteen.model.Food;
import com.example.ITCanteen.repo.CategoryRepository;
import com.example.ITCanteen.repo.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;

    public Food addFood(@Valid Food food) {
        if (food.getPrice() <= 0) {
            throw new IllegalArgumentException("Цена должна быть больше 0");
        }
        return foodRepository.save(food);
    }

    public Optional<Food> findById(Long id) {
        return foodRepository.findById(id);
    }

    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    public Food updateFood(Long id, @Valid Food updatedFood) {
        Food existingFood = foodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено с ID: " + id));
        existingFood.setName(updatedFood.getName());
        existingFood.setPrice(updatedFood.getPrice());
        existingFood.setCategory(updatedFood.getCategoryId());
        return foodRepository.save(existingFood);
    }

    public void deleteFood(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new IllegalArgumentException("Блюдо не найдено с ID: " + id);
        }
        foodRepository.deleteById(id);
    }

    public Optional<Category> findByCategoryId(Long categoryId) {
        return categoryRepository.findCategoryById(categoryId);
    }
}