package com.atguigu.eduservice.controller.front;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/eduservice/teacherfront")
@CrossOrigin
@RestController
public class TeacherFrontController {
    @Autowired
    private EduTeacherService teacherServic;

    @Autowired
    private EduCourseService eduCourseService;

//  讲师列表的分页
//  使用map集合实现分页
    @PostMapping("getTeacherFrontList/{page}/{limit}")
    public R  pageInfo(@PathVariable Long page ,
                       @PathVariable Long limit ){

//
        Page<EduTeacher> pageTeacherList = new Page<>(page, limit);

        Map<String,Object> map =  teacherServic.frontTeacherPage(pageTeacherList);
        return R.ok().data(map);
    }


//    查看讲师的详细信息 与讲师下的详细课程
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherDetalisget(@PathVariable String teacherId){

        EduTeacher teacher = teacherServic.getById(teacherId);

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
//        根据老师id查询 老师讲的课程信息
        List<EduCourse> courseList = eduCourseService.list(wrapper);

        return R.ok().data("teacher",teacher).data("courseList",courseList);
    }

}
