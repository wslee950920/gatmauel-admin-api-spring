package com.gatmauel.admin.service.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
@Service
public class UploadServiceImpl implements UploadService{
    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadImg(MultipartFile multipartFile) throws IOException {
        String originalName=multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());

        PutObjectRequest s3Request = new PutObjectRequest(this.bucketName, "food/"+originalName, multipartFile.getInputStream(), metadata);
        s3Client.putObject(s3Request);

        return originalName;
    }
}
