package com.example.ITCanteen.repo;

import com.example.ITCanteen.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food , Long> {
}
