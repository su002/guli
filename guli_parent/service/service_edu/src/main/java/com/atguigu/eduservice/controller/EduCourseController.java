package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-03
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    EduCourseService eduCourseService;

//    课程列表
    @GetMapping
    public R getAllCourse(){
        List<EduCourse> list = eduCourseService.list(null);
        return R.ok().data("list",list);
    }


    //添加课程基本信息的方法
    @RequestMapping("addCourseInfo")
    public R addCourse(@RequestBody CourseInfoVo courseInfoVo)  {

       String id =  eduCourseService.insertCourse(courseInfoVo);

        return R.ok().data("courseId",id);
    }

    //根据课程id查询课程基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseById(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = eduCourseService.getCourseById(courseId);

        return R.ok().data("courseInfoVo",courseInfoVo);

    }


    //修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourse(@RequestBody CourseInfoVo courseInfoVo) {
        eduCourseService.updateCourse(courseInfoVo);
        return R.ok();
    }


    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R selectAllByChapterId(@PathVariable String id){
        CoursePublishVo coursePublishVo = eduCourseService.selectAllByChapterId(id);

        return R.ok().data("publishCourse",coursePublishVo);
    }



// 课程最终发布

    // 修改课程状态
    @PostMapping("publishCourse/{id}") 
    public R updateCourseStatus(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }

    //    删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId){
        eduCourseService.deleteCourseById(courseId);
        return R.ok();
    }

}

