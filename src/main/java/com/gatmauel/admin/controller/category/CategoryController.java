package com.gatmauel.admin.controller.category;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.gatmauel.admin.dto.category.CategoryDTO;
import com.gatmauel.admin.service.category.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Log4j2
@RequestMapping("/@admin/category")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping(value="/register", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> register(@RequestBody CategoryDTO dto){
        return new ResponseEntity<>(categoryService.register(dto), HttpStatus.OK);
    }

    @GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> getList(){
        return new ResponseEntity<>(categoryService.getList(), HttpStatus.OK);
    }

    @PutMapping(value="/update/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> modify(@PathVariable(value="id") Long id, @RequestBody CategoryDTO dto){
        return new ResponseEntity<>(categoryService.modify(id, dto), HttpStatus.OK);
    }

    @DeleteMapping(value="/remove/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> remove(@PathVariable(value="id") Long id){
        return new ResponseEntity<>(categoryService.deleteWithFood(id), HttpStatus.OK);
    }
}