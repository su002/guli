package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-02
 */
@RestController
@CrossOrigin  //使用注解解决跨域
@RequestMapping("/eduservice/subject")
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;



    @PostMapping("/addSubject")
    public R saveEduSubject(MultipartFile file){

        subjectService.saveEduSubject(file , subjectService);
        return R.ok();
    }

//    获取一级分类 , 与一级分类下的二级分类
    @GetMapping("/getAllSubject")
    public R selectTwoAndOneSubject(){
        List<OneSubject> oneSubjectList  = subjectService.getAllOneTwoSubject();
        return R.ok().data("list" , oneSubjectList);
    }


}

