package com.gatmauel.admin.service;

import com.gatmauel.admin.dto.food.FoodDTO;
import com.gatmauel.admin.entity.category.Category;

import com.gatmauel.admin.entity.food.Food;
import com.gatmauel.admin.repository.category.CategoryRepository;
import com.gatmauel.admin.repository.food.FoodRepository;
import com.gatmauel.admin.service.food.FoodService;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class FoodServiceTest {
    @Autowired
    FoodRepository foodRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    FoodService foodService;

    private Category category;

    @Before
    public void insertCategory(){
        String kal="칼국수";
        int prior=1;

        category=Category.builder().
                category(kal).
                prior(prior).
                build();
        categoryRepository.save(category);
    }

    @After
    public void cleanup() {
        foodRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Transactional
    @Test
    public void testInsertFood(){
        String name="칼국수";
        String img="test.jpg";
        int price=7000;
        int prior=1;
        String comp="test";
        Long categoryId=category.getId();

        FoodDTO dto=FoodDTO.builder().
                name(name).
                img(img).
                price(price).
                prior(prior).
                comp(comp).
                categoryId(categoryId).build();

        FoodDTO result=foodService.register(dto);
        log.debug(result);

        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getImg()).isEqualTo(img);
        assertThat(result.getPrice()).isEqualTo(price);
        assertThat(result.getPrior()).isEqualTo(prior);
        assertThat(result.getComp()).isEqualTo(comp);
        assertThat(result.isDeli()).isEqualTo(false);
    }

    @Test
    public void testGetFoodList(){
        IntStream.rangeClosed(1, 3).forEach(i->{
            FoodDTO dto=FoodDTO.builder().
                    name("test"+i).
                    img("test"+i+".jpg").
                    price(i*1000).
                    prior(4-i).
                    comp("test"+i).
                    categoryId(category.getId()).build();

            FoodDTO result=foodService.register(dto);
            log.debug(result);
        });

        List<FoodDTO> dtoList=foodService.getList();
        log.debug(dtoList);
        assertTrue(dtoList.get(0).getPrior()<dtoList.get(dtoList.size()-1).getPrior());
    }

    @Test
    public void testModifyFood(){
        String name="칼국수";
        String img="test.jpg";
        int price=7000;
        int prior=1;
        String comp="test";
        Long categoryId=category.getId();

        FoodDTO dto=FoodDTO.builder().
                name(name).
                img(img).
                price(price).
                prior(prior).
                comp(comp).
                categoryId(categoryId).build();
        dto=foodService.register(dto);

        price=8000;
        dto.setPrice(price);
        log.debug(dto);

        Map<String, Object> result=foodService.modify(dto.getId(), dto);
        assertThat(result.get("price")).isEqualTo(price);
    }

    @Transactional
    @Test
    public void testRemoveFood(){
        String name="칼국수";
        String img="test.jpg";
        int price=7000;
        int prior=1;
        String comp="test";
        Long categoryId=category.getId();

        FoodDTO dto=FoodDTO.builder().
                name(name).
                img(img).
                price(price).
                prior(prior).
                comp(comp).
                categoryId(categoryId).build();
        dto=foodService.register(dto);
        List<Food> foodList=foodRepository.findAll();
        log.debug("foodList1:"+foodList);
        assertTrue(foodList.size()>0);

        Map<String, Long> result=foodService.remove(dto.getId());
        log.debug(result);
        assertThat(result.get("deleted")).isEqualTo(dto.getId());

        foodList=foodRepository.findAll();
        log.debug("foodList2:"+foodList);
        assertThat(foodList.size()).isEqualTo(0);

        foodList=categoryRepository.getById(categoryId).getFoodList();
        log.debug("foodList3:"+foodList);
        assertThat(foodList.size()).isEqualTo(0);
    }
}