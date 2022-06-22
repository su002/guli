package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-03
 */
public interface EduChapterService extends IService<EduChapter> {

//        根据课程id 查询章节下面的小节
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    Boolean deleteChapter(String courseId);

    void addChapter( EduChapter eduChapter);

    EduChapter selectChapterById(String chapterId);

    void updateChapter(EduChapter eduChapter);

//    根据课程id删除课程的章节
    void removeChapterById(String courseId);
}
