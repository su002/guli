package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.courseQueryVo.CourseFrontVo;
import com.atguigu.eduservice.entity.courseQueryVo.courseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-03
 */
public interface EduCourseService extends IService<EduCourse> {

    String insertCourse(CourseInfoVo courseInfoVo) ;

    CourseInfoVo getCourseById(String courseId);

    void updateCourse(CourseInfoVo courseInfoVo);

    CoursePublishVo selectAllByChapterId(String id);

//    删除课程
    void deleteCourseById(String courseId);

    //    条件查询带分页查询 ,查询课程详情
    Map<String, Object> selectFrontCourseList(CourseFrontVo courseFrontVo, Page<EduCourse> coursePage);


//    查询课程的详情
    courseWebVo getCourseByCourseId(String courseId);
}
