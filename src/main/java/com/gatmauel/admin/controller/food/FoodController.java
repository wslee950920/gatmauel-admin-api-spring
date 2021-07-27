package com.gatmauel.admin.controller.food;

import com.gatmauel.admin.dto.food.FoodDTO;
import com.gatmauel.admin.dto.food.FoodMultipartRequestDTO;
import com.gatmauel.admin.service.food.FoodService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/@admin/food")
@RequiredArgsConstructor
@Log4j2
@RestController
public class FoodController {
    private final FoodService foodService;

    @PostMapping(value="/register", produces=MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> register(@ModelAttribute FoodMultipartRequestDTO requestDTO) {
        try{
            return new ResponseEntity<>(foodService.register(requestDTO), HttpStatus.OK);
        } catch(IllegalArgumentException e){
            Map<String, Object> error = new HashMap<>();
            HttpStatus status;

            error.put("code", HttpStatus.BAD_REQUEST.value());
            error.put("message", e.getMessage());

            status = HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(error, status);
        } catch(Exception e){
            Map<String, Object> error = new HashMap<>();
            HttpStatus status;

            error.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            error.put("message", e.getMessage());

            status = HttpStatus.INTERNAL_SERVER_ERROR;

            return new ResponseEntity<>(error, status);
        }
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FoodDTO>> getList(){
        return new ResponseEntity<>(foodService.getList(), HttpStatus.OK);
    }

    @PutMapping(value="/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> modify(@PathVariable(value = "id") Long id, @RequestBody FoodDTO dto){
        return new ResponseEntity<>(foodService.modify(id, dto), HttpStatus.OK);
    }

    @DeleteMapping(value="/remove/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> remove(@PathVariable(value = "id") Long id){
        return new ResponseEntity<>(foodService.remove(id), HttpStatus.OK);
    }
}
