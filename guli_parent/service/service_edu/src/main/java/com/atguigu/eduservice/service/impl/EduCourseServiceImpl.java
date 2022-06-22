package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.courseQueryVo.CourseFrontVo;
import com.atguigu.eduservice.entity.courseQueryVo.courseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;

import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-03
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private EduCourseDescriptionService descriptionService;
    @Autowired
    private  EduCourseMapper courseMapper;

    @Override
    public String insertCourse(CourseInfoVo courseInfoVo){

        EduCourse eduCourse = new EduCourse();
//       将eduCourse 里面的属性 设置到 CourseInfoVo
        eduCourse.setSubjectId(courseInfoVo.getSubjectId());
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert == 0) {
            throw new GuliException(20001,"课程添加失败");
        }
//        获取课程id , 讲师 与课程对应 , 添加课程时 , 需要在讲师列表添加课程id
        String Courseid = eduCourse.getId();
//        添加课程信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
//        从前端提交的信息 ,获取课程信息 , 设置到 EduCourseDescription类中
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
//      获取课程id  课程信息与课程一一对应 , eduCourse.getId() 将

        eduCourseDescription.setId(Courseid);
        courseDescriptionService.save(eduCourseDescription);

        return Courseid;
    }


//    根据课程id 查询课程信息
    @Override
    public CourseInfoVo getCourseById(String courseId) {
//        查询课程信息
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse, courseInfoVo);

//        查询课程简介
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);

        String description = courseDescription.getDescription();
        courseInfoVo.setId(courseDescription.getId());
        courseInfoVo.setDescription(description);
        return courseInfoVo;
    }



//    修改课程信息
    @Override
    public void updateCourse(CourseInfoVo courseInfoVo) {
//       修改课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update < 1) {
            throw new GuliException(20001,"修改课程信息失败");
        }

//        修改简介
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(courseInfoVo.getId());
        courseDescription.setDescription(courseInfoVo.getDescription());

//        BeanUtils.copyProperties(courseInfoVo, courseDescription);

        courseDescriptionService.updateById(courseDescription);

    }


//    课程信息确认
    @Override
    public CoursePublishVo selectAllByChapterId(String id) {
        CoursePublishVo coursePublishVo = baseMapper.selectAllByChapterId(id);

        return coursePublishVo;
    }


//    删除课程 根据课程id删除
    @Transactional  //添加事务
    @Override
    public void deleteCourseById(String courseId) {
//        第一步 : 删除小节
        eduVideoService.removeVideoById(courseId);

//        第二步 : 删除章节
        eduChapterService.removeChapterById(courseId);

//        第三步 : 删除描述
        courseDescriptionService.removeDescriptionById(courseId);


//        第四步:  删除课程
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        int result = baseMapper.delete(queryWrapper);
        if (result == 0) {
            throw new GuliException(20001,"删除课程失败");
        }
    }



//    条件查询带分页查询 ,查询课程详情

//    根据课程Id 查询课程详情
    @Override
    public Map<String, Object> selectFrontCourseList(CourseFrontVo courseFrontVo, Page<EduCourse> pageTeacherList) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) {
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())) {
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }

        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) {
            wrapper.orderByDesc("price");       //按照价格
        }
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){
            wrapper.orderByDesc("buy_count");  //按照阅读量
        }
        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) {
            wrapper.orderByDesc("gmt_create");   //按照最新排序

        }


        baseMapper.selectPage(pageTeacherList,wrapper);
        //        获取对应的值
        List<EduCourse> records = pageTeacherList.getRecords();
        long total = pageTeacherList.getTotal();
        long pages = pageTeacherList.getPages();
        long current = pageTeacherList.getCurrent();
        long size = pageTeacherList.getSize();

//        是否有上一页 , 下一页
        boolean next = pageTeacherList.hasNext();
        boolean previous = pageTeacherList.hasPrevious();

        Map<String ,Object> map = new HashMap<>();
        map.put("items",records);
        map.put("total",total);
        map.put("pages",pages);
        map.put("current",current);
        map.put("size",size);
        map.put("hasNext",next);
        map.put("hasPrevious",previous);


        return map;

    }

    @Override
    public courseWebVo getCourseByCourseId(String courseId) {

        courseWebVo courseWeb = courseMapper.getCourseByCourseId(courseId);

        return courseWeb;
    }


}
