package com.gatmauel.admin.service.category;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gatmauel.admin.dto.category.CategoryDTO;
import com.gatmauel.admin.dto.food.FoodDTO;
import com.gatmauel.admin.entity.category.Category;
import com.gatmauel.admin.entity.food.Food;

public interface CategoryService {
    CategoryDTO register(CategoryDTO dto);

    List<CategoryDTO> getList();

    Map<String, Object> modify(Long id, CategoryDTO dto);

    Map<String, Long> deleteWithFood(Long id);

    default Category dtoToEntity(CategoryDTO dto) {
        Category category=Category.builder().
                prior(dto.getPrior()).
                category(dto.getCategory()).build();

        return category;
    }

    default CategoryDTO entityToDTO(Category category) {
        List<Food> foodList=category.getFoodList();

        List<FoodDTO> dtoList=foodList.stream().map(f->
                FoodDTO.builder().
                        id(f.getId()).
                        name(f.getName()).
                        img(f.getImg()).
                        price(f.getPrice()).
                        prior(f.getPrior()).
                        deli(f.isDeli()).
                        comp(f.getComp()).
                        categoryId(category.getId()).build()
        ).collect(Collectors.toList()).
                stream().sorted(Comparator.comparing(FoodDTO::getPrior)).
                collect(Collectors.toList());

        CategoryDTO dto=CategoryDTO.builder().
                id(category.getId()).
                category(category.getCategory()).
                prior(category.getPrior()).
                foodList(dtoList).build();

        return dto;
    }
}