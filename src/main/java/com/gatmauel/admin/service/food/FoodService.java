package com.gatmauel.admin.service.food;

import com.gatmauel.admin.dto.food.FoodDTO;
import com.gatmauel.admin.entity.food.Food;

import java.util.List;
import java.util.Map;

public interface FoodService {
    FoodDTO register(FoodDTO dto);

    List<FoodDTO> getList();

    Map<String, Object> modify(Long id, FoodDTO dto);

    Map<String, Long> remove(Long id);

    default Food dtoToEntity(FoodDTO dto){
        Food food=Food.builder().
                name(dto.getName()).
                img(dto.getImg()).
                price(dto.getPrice()).
                prior(dto.getPrior()).
                comp(dto.getComp()).
                deli(dto.isDeli()).build();

        return food;
    }

    default FoodDTO entityToDTO(Food food){
        FoodDTO dto=FoodDTO.builder().
                id(food.getId()).
                name(food.getName()).
                price(food.getPrice()).
                img(food.getImg()).
                prior(food.getPrior()).
                comp(food.getComp()).
                deli(food.isDeli()).
                categoryId(food.getCategory().getId()).build();

        return dto;
    }
}
