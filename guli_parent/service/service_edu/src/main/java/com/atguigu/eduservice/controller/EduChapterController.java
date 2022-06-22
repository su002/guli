package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
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
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {
    @Autowired
    private EduChapterService chapterService;

    @GetMapping("/getChapterVideo/{courseId}")
    public R addChapter(@PathVariable String courseId) {

        List<ChapterVo> chapterVos = chapterService.getChapterVideoByCourseId(courseId);

        return R.ok().data("allChapterVideo", chapterVos);
    }

//   添加章节
    @PostMapping("addChapter")
    public  R addChapter(@RequestBody EduChapter eduChapter) {
        chapterService.addChapter(eduChapter);
        return R.ok();
    }

//    根据章节id查询
    @GetMapping("/getChapterInfo/{chapterId}")
    public R selectChapterById(@PathVariable String chapterId){
        EduChapter eduChapter = chapterService.selectChapterById(chapterId);

        return R.ok().data("chapter",eduChapter);
    }


    @PostMapping("deleteChapter/{courseId}")
    public R deleteChapter(@PathVariable String courseId) {
        Boolean flg = chapterService.deleteChapter(courseId);
        if (flg == false) {
            return R.error();
        }
        return R.ok();
    }


//    修改章节
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter) {
        chapterService.updateChapter(eduChapter);

        return R.ok();
    }

}

