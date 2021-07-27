package com.gatmauel.admin.dto.food;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FoodMultipartRequestDTO extends FoodDTO{
    private MultipartFile file;
}
