package com.gatmauel.admin.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.gatmauel.admin.entity.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @EntityGraph(attributePaths= {"foodList"}, type=EntityGraph.EntityGraphType.FETCH)
    List<Category> findByOrderByPriorAsc();

    @Modifying
    void deleteById(Long id);
}