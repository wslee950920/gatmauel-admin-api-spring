package com.gatmauel.admin.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gatmauel.admin.dto.category.CategoryDTO;
import com.gatmauel.admin.entity.category.Category;
import com.gatmauel.admin.entity.food.Food;
import com.gatmauel.admin.repository.category.CategoryRepository;
import com.gatmauel.admin.repository.food.FoodRepository;
import com.gatmauel.admin.service.category.CategoryService;

import lombok.extern.log4j.Log4j2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FoodRepository foodRepository;

    @After
    public void cleanup() {
        foodRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Transactional
    @Test
    public void testRegister() {
        String category="test";
        int prior=9;
        CategoryDTO dto=CategoryDTO.builder().
                category(category).
                prior(prior).build();

        CategoryDTO result=categoryService.register(dto);
        log.debug(result);

        assertThat(result.getCategory()).isEqualTo(category);
        assertThat(result.getPrior()).isEqualTo(prior);
    }

    @Test
    public void testGetList() {
        String kal="칼국수";
        int prior=1;
        int num=3;

        Category category=Category.builder().
                category(kal).
                prior(prior).
                build();

        IntStream.rangeClosed(1, num).forEach(i->{
            category.addFood(Food.builder().
                    name("test"+i).
                    img("test"+i+".jpg").
                    price(i*1000).
                    prior(i).build());
        });

        categoryRepository.save(category);

        List<CategoryDTO> dtoList=categoryService.getList();
        log.debug(dtoList);

        CategoryDTO dto=dtoList.get(0);

        assertThat(dto.getCategory()).isEqualTo(kal);
        assertThat(dto.getPrior()).isEqualTo(prior);
        assertThat(dto.getFoodList().size()).isEqualTo(num);
    }

    @Transactional
    @Test
    public void testModify() {	//만약 속성 하나만 dto에 넣어주면 dto에 안 넣은 속성은 default값으로 덮어씌어진다.
        String kal="kal";
        int prior=1;

        Category category=Category.builder().
                category(kal).
                prior(prior).
                build();
        categoryRepository.save(category);

        String jeong="jeong";
        CategoryDTO dto=CategoryDTO.builder().
                category(jeong).
                prior(prior).build();
        Map<String, Object> result=categoryService.modify(category.getId(), dto);
        log.debug("result : "+result);

        assertThat(result.get("category")).isEqualTo(jeong);
        assertThat(result.get("prior")).isEqualTo(prior);
        assertThat(result.get("updated")).isEqualTo(category.getId());
    }

    @Test
    public void deleteCascade() {
        String kal="칼국수";
        int prior=1;
        int num=3;

        Category category=Category.builder().
                category(kal).
                prior(prior).
                build();
        IntStream.rangeClosed(1, num).forEach(i->{
            category.addFood(Food.builder().
                    name("test"+i).
                    img("test"+i+".jpg").
                    price(i*1000).
                    prior(i).build());
        });
        categoryRepository.save(category);
        log.debug(category.getFoodList());

        categoryService.deleteWithFood(category.getId());

        List<Category> categoryList=categoryRepository.findAll();
        log.debug(categoryList);
        assertThat(categoryList.size()).isEqualTo(0);
        List<Food> foodList=foodRepository.findAll();
        log.debug(foodList);
        assertThat(foodList.size()).isEqualTo(0);
    }
}