package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.controller.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/eduoss")
@CrossOrigin  //使用注解解决跨域
public class OssController {
    @Autowired
    private OssService ossService;


    @PostMapping("/fileoss")
    public R OssFileOs(MultipartFile file){
       String url =  ossService.ossFileos(file);
        return R.ok().data("url",url);
    }
}
