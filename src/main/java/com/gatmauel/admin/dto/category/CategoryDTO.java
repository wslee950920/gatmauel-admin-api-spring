package com.gatmauel.admin.dto.category;

import com.gatmauel.admin.dto.food.FoodDTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;

    private String category;

    private int prior;

    private List<FoodDTO> foodList;
}