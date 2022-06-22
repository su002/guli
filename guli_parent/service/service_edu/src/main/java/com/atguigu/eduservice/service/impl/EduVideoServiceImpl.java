package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.ClientService;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-03
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    private ClientService clientService;

//    根据课程id 删除课程
    @Override
    public void removeVideoById(String courseId) {
        QueryWrapper<EduVideo> videoWapper = new QueryWrapper<>();
//        根据课程 id 查询课程信息, 获取其中的 视频的id
        videoWapper.eq("course_id",courseId);
        List<EduVideo> eduVideoList = baseMapper.selectList(videoWapper);
        List<String> list = new ArrayList<>();
        for (EduVideo eduVideo : eduVideoList) {
//            获取视频的id , 添加到集合中
            String videoSourceId = eduVideo.getVideoSourceId();
            if (!StringUtils.isEmpty(videoSourceId)){
                list.add(videoSourceId);
            }
        }
//        调用client 的接口实现删除
        if (!list.isEmpty()) {
            clientService.deleteVideo(list);
        }
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id" ,courseId);
        baseMapper.delete(queryWrapper);
    }
}
