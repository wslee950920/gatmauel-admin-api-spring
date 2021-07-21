package com.gatmauel.admin.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gatmauel.admin.entity.category.Category;
import com.gatmauel.admin.entity.food.Food;
import com.gatmauel.admin.repository.category.CategoryRepository;
import com.gatmauel.admin.repository.food.FoodRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoodRepositoryTest {
    @Autowired
    FoodRepository foodRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private Category category;

    @Before
    public void insertCategory() {
        category=Category.builder().
                category("test").
                prior(1).build();
        categoryRepository.save(category);
    }

    @After
    public void cleanup() {
        foodRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void testInsertDefaultAndSelectFood() {
        String name="칼국수";
        String img="test.jpg";
        int price=7000;
        int prior=1;
        String comp="test";

        foodRepository.save(Food.builder().
                name(name).
                img(img).
                price(price).
                prior(prior).
                comp(comp).
                category(category).build());

        List<Food> foodList=foodRepository.findAll();
        Food food=foodList.get(0);

        assertThat(food.getName()).isEqualTo(name);
        assertThat(food.getImg()).isEqualTo(img);
        assertThat(food.getPrice()).isEqualTo(price);
        assertThat(food.getPrior()).isEqualTo(prior);
        assertThat(food.getCategory().getId()).isEqualTo(category.getId());
        assertThat(food.getComp()).isEqualTo(comp);
        assertThat(food.isDeli()).isEqualTo(false);
    }

    @Test
    public void testInsertAndSelectFood() {
        String name="보쌈정식";
        String img="test.jpg";
        int price=12000;
        boolean deli=true;
        int prior=2;
        String comp="test";

        foodRepository.save(Food.builder().
                name(name).
                img(img).
                price(price).
                prior(prior).
                category(category).
                comp(comp).
                deli(deli).build());

        List<Food> foodList=foodRepository.findAll();
        Food food=foodList.get(0);

        assertThat(food.getName()).isEqualTo(name);
        assertThat(food.getImg()).isEqualTo(img);
        assertThat(food.getPrice()).isEqualTo(price);
        assertThat(food.getPrior()).isEqualTo(prior);
        assertThat(food.getCategory().getId()).isEqualTo(category.getId());
        assertThat(food.isDeli()).isEqualTo(deli);
        assertThat(food.getComp()).isEqualTo(comp);
    }
}