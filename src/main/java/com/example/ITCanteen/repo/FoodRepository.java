package com.example.ITCanteen.repo;

import com.example.ITCanteen.model.Category;
import com.example.ITCanteen.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food , Long> {
    Optional<Food> findByCategory(Category category);
    List<Food> findByCategory_Id(Long categoryId);
    int countByCategory(Category category);
}
