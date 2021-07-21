package com.gatmauel.admin.repository.food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gatmauel.admin.entity.category.Category;
import com.gatmauel.admin.entity.food.Food;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    @Modifying
    @Query("DELETE FROM Food f WHERE f.category=:category")
    void deleteByCategory(@Param("category") Category category);

    List<Food> findByOrderByPriorAsc();
}