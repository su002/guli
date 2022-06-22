package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourseDescription;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-03
 */
public interface EduCourseDescriptionService extends IService<EduCourseDescription> {

//    根据课程id 删除课程的描述信息
    void removeDescriptionById(String courseId);

}
