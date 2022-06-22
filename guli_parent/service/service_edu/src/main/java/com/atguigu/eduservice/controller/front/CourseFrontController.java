package com.atguigu.eduservice.controller.front;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.courseQueryVo.CourseFrontVo;
import com.atguigu.eduservice.entity.courseQueryVo.courseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/eduservice/coursefront")
public class CourseFrontController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;

//    条件查询带分页查询 ,查询课程详情
    @PostMapping("/getFrontCourseList/{page}/{limit}")
    public R getCourseFront(@RequestBody(required = false) CourseFrontVo courseFrontVo,
                            @PathVariable Long page,
                            @PathVariable Long limit
                            ) {

        Page<EduCourse> coursePage = new Page<>(page,limit);
        Map<String,Object> map = courseService.selectFrontCourseList(courseFrontVo,coursePage);

        return R.ok().data(map);

    }



//    课程详情的查询
    @GetMapping("getFrontCourseInfo/{id}")
    public R getCourseByCourseId(@PathVariable String id){
//        根据课程 id 查询 课程信息
        courseWebVo courseWeb = courseService.getCourseByCourseId(id);

//        根据课程id 查询章节下面的小节
        List<ChapterVo> chapterVoList = chapterService.getChapterVideoByCourseId(id);

        return R.ok().data("courseWebVo",courseWeb).data("chapterVideoList",chapterVoList);
    }




    /**
     *     根据课程 id 查询 讲师 ,课程相关信息
     * @param id
     * @return
     */

    @RequestMapping("getCourseInfo/{id}")
    public courseWebVo getCourseInfo(@PathVariable String id) {

//        获取返回结果 , 用于支付接口的调用
        courseWebVo course = courseService.getCourseByCourseId(id);
        return course;
    }



}
