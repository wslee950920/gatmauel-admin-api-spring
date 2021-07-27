package com.gatmauel.admin.service.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    String uploadImg(MultipartFile multipartFile) throws IOException;
}
