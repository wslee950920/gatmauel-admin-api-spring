package com.gatmauel.admin.controller;

import com.gatmauel.admin.dto.food.FoodDTO;
import com.gatmauel.admin.entity.category.Category;
import com.gatmauel.admin.repository.category.CategoryRepository;
import com.gatmauel.admin.repository.food.FoodRepository;
import com.gatmauel.admin.service.common.UploadService;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class FoodControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private UploadService uploadService;

    @Before
    public void setup(){
        String kal="칼국수";
        int prior=1;

        categoryRepository.save(Category.builder().
                category(kal).
                prior(prior).
                build());
    }

    @After
    public void cleanup(){
        foodRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void fail_register_food_not_authourized(){
        String name="칼국수";
        String img="kal.jpg";
        int price=7000;
        int prior=1;
        boolean deli=true;
        String comp="test";
        Long categoryId=1L;

        FoodDTO dto=FoodDTO.builder()
                .name(name)
                .img(img)
                .price(price)
                .prior(prior)
                .deli(deli)
                .comp(comp)
                .categoryId(categoryId).build();
    }
}
