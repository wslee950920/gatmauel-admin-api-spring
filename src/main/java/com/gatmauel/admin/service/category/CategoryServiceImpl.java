package com.gatmauel.admin.service.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gatmauel.admin.dto.category.CategoryDTO;
import com.gatmauel.admin.entity.category.Category;
import com.gatmauel.admin.repository.category.CategoryRepository;
import com.gatmauel.admin.repository.food.FoodRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final FoodRepository foodRepository;

    @Override
    public CategoryDTO register(CategoryDTO dto) {
        Category category=dtoToEntity(dto);
        log.debug(category);
        categoryRepository.save(category);

        return entityToDTO(category);
    }

    @Override
    public List<CategoryDTO> getList(){
        List<CategoryDTO> dtoList=new ArrayList<>();

        List<Category> categoryList=categoryRepository.findByOrderByPriorAsc();
        categoryList.forEach(category->{
            dtoList.add(entityToDTO(category));
        });

        return dtoList;
    }

    @Transactional
    @Override
    public Map<String, Object> modify(Long id, CategoryDTO dto) {
        Category category=categoryRepository.getById(id);

        category.changeCategory(dto.getCategory());
        category.changePrior(dto.getPrior());

        categoryRepository.save(category);

        Map<String, Object> result=new HashMap<>();
        result.put("updated", category.getId());
        result.put("category", category.getCategory());
        result.put("prior", category.getPrior());

        return result;
    }

    @Transactional
    @Override
    public Map<String, Long> deleteWithFood(Long id) {
        Category category=Category.builder().
                id(id).build();
        log.debug(category);

        foodRepository.deleteByCategory(category);
        categoryRepository.deleteById(id);

        Map<String, Long> result=new HashMap<>();
        result.put("deleted", id);

        return result;
    }
}