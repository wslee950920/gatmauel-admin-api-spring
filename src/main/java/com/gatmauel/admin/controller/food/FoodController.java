package com.gatmauel.admin.controller.food;

import com.gatmauel.admin.config.aws.AWSConfig;
import com.gatmauel.admin.dto.food.FoodDTO;
import com.gatmauel.admin.service.food.FoodService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping("/@admin/food")
@RequiredArgsConstructor
@Log4j2
@RestController
public class FoodController {
    private final FoodService foodService;

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @PostMapping(value = "/register", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodDTO> register(HttpServletRequest request) throws AmazonServiceException, SdkClientException, IOException {
            MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;

            MultipartFile multipartFile=multipartRequest.getFile("img");
            String originalName=multipartFile.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());

            PutObjectRequest s3Request = new PutObjectRequest(bucketName, "food/"+originalName, multipartFile.getInputStream(), metadata);
            s3Client.putObject(s3Request);

            FoodDTO dto=FoodDTO.builder().
                    name(request.getParameter("name")).
                    img(originalName).
                    price(Integer.parseInt(request.getParameter("price"))).
                    prior(Integer.parseInt(request.getParameter("prior"))).
                    deli(Boolean.parseBoolean(request.getParameter("deli"))).
                    comp(request.getParameter("comp")).
                    categoryId(Long.parseLong(request.getParameter("categoryId"))).build();
            log.debug(dto);

            return new ResponseEntity<>(foodService.register(dto), HttpStatus.OK);
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
