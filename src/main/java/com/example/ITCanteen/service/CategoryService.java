package com.example.ITCanteen.service;

import com.example.ITCanteen.model.Category;
import com.example.ITCanteen.repo.CategoryRepository;
import com.example.ITCanteen.repo.FoodRepository;
import com.example.ITCanteen.dto.CategoryWithCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final FoodRepository foodRepository;

    public Category createCategory(@Valid Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Long id, @Valid Category updatedCategory) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена с ID: " + id));
        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Категория не найдена с ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    public List<CategoryWithCountDto> getCategoriesWithCount() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryWithCountDto> result = new java.util.ArrayList<>();
        for (Category category : categories) {
            int count = foodRepository.countByCategory(category);
            result.add(new CategoryWithCountDto(category.getId(), category.getName(), category.getDescription(), count));
        }
        return result;
    }
}