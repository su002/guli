package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-02-24
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {
    @Autowired
    private EduTeacherService eduTeacherService;

    @Cacheable(key = "teacherList",value = "'getIndexTeacherList'")
    @Override
    public List<EduTeacher> getIndexTeacherList() {
        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByDesc("id");
        teacherWrapper.last("limit 4");

        List<EduTeacher> teacherList = baseMapper.selectList(teacherWrapper);
//        List<EduTeacher> teacherList = eduTeacherService.list(teacherWrapper);
        return teacherList;
    }



    @Override
    public Map<String, Object> frontTeacherPage(Page<EduTeacher> pageTeacherList) {
//        对列表做排序
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
//        排序条件
        wrapper.orderByDesc("id");

//        将查询出的讲师    ,封装到 pageTeacherList 中
        baseMapper.selectPage(pageTeacherList,wrapper);


//        获取对应的值
        List<EduTeacher> records = pageTeacherList.getRecords();
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
}
