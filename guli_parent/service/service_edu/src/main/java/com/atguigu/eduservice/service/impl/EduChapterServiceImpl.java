package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        Map<String, ChapterVo> map = new HashMap<>();

        List<ChapterVo> allList = new ArrayList<>();
//        查询出所有的章节
        QueryWrapper<EduChapter> chapterVoWrapper = new QueryWrapper<>();
        chapterVoWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(chapterVoWrapper);


//        查询子章节
        QueryWrapper<EduVideo> chapterVoWrapperVoide = new QueryWrapper<>();
        chapterVoWrapperVoide.eq("course_id", courseId);
//        查询出所有子章节
        List<EduVideo> eduVideoList = eduVideoService.list(chapterVoWrapperVoide);

//遍历父章节
        String id =null;
        List<ChapterVo> finallyChapterVoList = new ArrayList<>();

        for (EduChapter chapter : eduChapters) {

            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);

            id =  chapter.getCourseId();
//            System.out.println(id);
            // 将遍历结果放入到map


            map.put(id, chapterVo);
//            System.out.println("++++++++++++++++="+chapterVo);
//            将最终结果添加到集合
            allList.add(chapterVo);
        }


//        遍历子章节
        for (EduVideo eduVideo:eduVideoList) {
            List<VideoVo> videoVoList = new ArrayList<>();
            VideoVo videoVo = new VideoVo();
            String courseIdByEduVideoId = eduVideo.getCourseId();
            if (courseIdByEduVideoId.equals(id)) {
                BeanUtils.copyProperties(eduVideo, videoVo);
                ChapterVo chapterVo = map.get(courseIdByEduVideoId);

                videoVoList.add(videoVo);

                chapterVo.getChildren().add(videoVo);
                System.out.println(videoVoList);
            }else {
                throw new GuliException(20001 ,"错误");
            }
        }
        return allList;
    }


//课程大纲列表,根据课程id进行查询
//@Override
//public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
//
//    //1 根据课程id查询课程里面所有的章节
//    QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
//    wrapperChapter.eq("course_id",courseId);
//    List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);
//
//    //2 根据课程id查询课程里面所有的小节
//    QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
//    wrapperVideo.eq("course_id",courseId);
//    List<EduVideo> eduVideoList = eduVideoService.list(wrapperVideo);
//
//    //创建list集合，用于最终封装数据
//    List<ChapterVo> finalList = new ArrayList<>();
//
//    //3 遍历查询章节list集合进行封装
//    //遍历查询章节list集合
//    for (int i = 0; i < eduChapterList.size(); i++) {
//        //每个章节
//        EduChapter eduChapter = eduChapterList.get(i);
//        //eduChapter对象值复制到ChapterVo里面
//        ChapterVo chapterVo = new ChapterVo();
//        BeanUtils.copyProperties(eduChapter,chapterVo);
//        //把chapterVo放到最终list集合
//        finalList.add(chapterVo);
//
//        //创建集合，用于封装章节的小节
//        List<VideoVo> videoList = new ArrayList<>();
//
//        //4 遍历查询小节list集合，进行封装
//        for (int m = 0; m < eduVideoList.size(); m++) {
//            //得到每个小节
//            EduVideo eduVideo = eduVideoList.get(m);
//            //判断：小节里面chapterid和章节里面id是否一样
//            if(eduVideo.getChapterId().equals(eduChapter.getId())) {
//                //进行封装
//                VideoVo videoVo = new VideoVo();
//                BeanUtils.copyProperties(eduVideo,videoVo);
//                //放到小节封装集合
//                videoList.add(videoVo);
//            }
//        }
//        //把封装之后小节list集合，放到章节对象里面
//        chapterVo.setChildren(videoList);
//    }
//    return finalList;
//}




    @Override
    public Boolean deleteChapter(String courseId) {

//        根据 chapter_id 查询 章节下面的子目录
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();

//        比较 两id 是否相等
        wrapper.eq("chapter_id"  ,courseId);

        int count = eduVideoService.count(wrapper);
        if (count > 0) {
            throw new GuliException(20001,"该章节下有子章节, 不能直接删除");
        }else {
            eduVideoService.removeById(courseId);
            return true;
        }
    }


    @Override
    public void addChapter(EduChapter eduChapter) {
        ChapterVo chapterVo = new ChapterVo();
        BeanUtils.copyProperties(eduChapter ,chapterVo);
        baseMapper.insert(eduChapter);

    }

//    根据章节id查询章节信息
    @Override
    public EduChapter selectChapterById(String chapterId) {
        EduChapter eduChapter = baseMapper.selectById(chapterId);

        return eduChapter;
    }

//    修改章节信息
    @Override
    public void updateChapter(EduChapter eduChapter) {
        baseMapper.updateById(eduChapter);
    }


//    根据课程id 删除章节信息
    @Override
    public void removeChapterById(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        baseMapper.delete(queryWrapper);
    }
}
