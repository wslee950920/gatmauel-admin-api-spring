package com.gatmauel.admin.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gatmauel.admin.entity.category.Category;
import com.gatmauel.admin.entity.food.Food;
import com.gatmauel.admin.repository.category.CategoryRepository;
import com.gatmauel.admin.repository.food.FoodRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    FoodRepository foodRepository;

    @After
    public void cleanup() {
        foodRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void testInsertAndSelectCategory() {
        String kal="칼국수";
        int prior=1;

        categoryRepository.save(Category.builder().
                category(kal).
                prior(prior).
                build());

        List<Category> categoryList=categoryRepository.findAll();
        Category category=categoryList.get(0);

        assertThat(category.getCategory()).isEqualTo(kal);
        assertThat(category.getPrior()).isEqualTo(prior);
    }

    @Test
    @Transactional
    public void testInsertAndSelectCategoryAndFoodTogether() {
        String kal="칼국수";
        int prior=1;
        int length=3;

        Category category=Category.builder().
                category(kal).
                prior(prior).
                build();

        IntStream.rangeClosed(1, length).forEach(i->{
            category.addFood(Food.builder().
                    name("test"+i).
                    img("test"+i+".jpg").
                    price(i*1000).
                    prior(i).
                    category(category).build());
        });

        categoryRepository.save(category);

        List<Category> categoryList=categoryRepository.findAll();
        categoryList.forEach(c->{
            log.debug(c.toString());
            log.debug(c.getFoodList().toString());

            assertThat(c.getFoodList().size()).isEqualTo(length);
            assertThat(c.getCategory()).isEqualTo(kal);
            assertThat(c.getPrior()).isEqualTo(prior);
        });

    }
}