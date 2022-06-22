package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-02
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveEduSubject(MultipartFile file,EduSubjectService subjectService);


    List<OneSubject> getAllOneTwoSubject();


    //    获取首页的课程信息
    List<EduSubject> getIndexEduSubjectList();
}
