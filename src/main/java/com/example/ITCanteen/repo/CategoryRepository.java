package com.example.ITCanteen.repo;

import com.example.ITCanteen.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category , Long> {
    Optional<Category> findCategoryById(Long categoryId);
}
