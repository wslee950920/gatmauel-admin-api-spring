package com.gatmauel.admin.dto.food;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO {
    private Long id;

    private String name;

    private String img;

    private int price;

    private int prior;

    private boolean deli;

    private String comp;

    private Long categoryId;
}
