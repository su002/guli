package com.atguigu.oss.controller.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String ossFileos(MultipartFile file);
}
