package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.ExcelPo.EduObjectExcel;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.EduSbjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.One;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-02
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {


    @Autowired
    private EduSubjectService eduSubjectService;


    //    获取首页的课程信息
    @Cacheable(key = "subList", value = "'getIndexEduSubjectList'")
    @Override
    public List<EduSubject> getIndexEduSubjectList() {
        //        查询前8的课程
        QueryWrapper<EduSubject> subWrapper = new QueryWrapper<>();
        subWrapper.orderByDesc("id");
        subWrapper.last("limit 8");
        List<EduSubject> eduList = eduSubjectService.list(subWrapper);
        return eduList;
    }


    @Override
    public void saveEduSubject(MultipartFile file, EduSubjectService subjectService) {
//        文件读取时流的方式
        try {
            InputStream inputfile = file.getInputStream();
            EasyExcel.read(inputfile, EduObjectExcel.class, new EduSbjectExcelListener(subjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        Map<String, OneSubject> map = new HashMap<>();

//        查询出所有的一级分类   , 根据parent_id 是否等于0查询
        QueryWrapper<EduSubject> oneSubject = new QueryWrapper<>();
        oneSubject.eq("parent_id", "0"); // 等于0
        List<EduSubject> oneList = baseMapper.selectList(oneSubject);

//        查询出所有二级分类
        QueryWrapper<EduSubject> twoObject = new QueryWrapper<>();
        twoObject.ne("parent_id", "0");
        List<EduSubject> twoList = baseMapper.selectList(twoObject);

//        将二级分类添加到一级分类
        String id = null;
        List<OneSubject> finallyoneSubjectList = new ArrayList<>();
        for (EduSubject oneEduSubject : oneList) {
//            获取所有一级分类的 id
            id = oneEduSubject.getId();
            OneSubject one = new OneSubject();
            map.put(id, one);


//         第一种方式:    把oneList 里面的一级分类 当到 OneSubject 里面
            BeanUtils.copyProperties(oneEduSubject, one);

//第二种方式:
//            one.setId(oneEduSubject.getId());
//            one.setTitle(oneEduSubject.getTitle());
            finallyoneSubjectList.add(one);
        }


        for (EduSubject twoEduSubject : twoList) {
            List<TwoSubject> twoFinalList = new ArrayList<>();
            TwoSubject twoSubject = new TwoSubject();
            String parentId = twoEduSubject.getParentId();
            OneSubject oneSubject1 = map.get(parentId);

            BeanUtils.copyProperties(twoEduSubject, twoSubject);
            twoFinalList.add(twoSubject);
//                one.setTwoList(twoFinalList);
            oneSubject1.getChildren().add(twoSubject);
            System.out.println(twoFinalList);
        }
//        System.out.println(finallyoneSubjectList);
        return finallyoneSubjectList;
    }


//课程分类列表（树形）
//@Override
//public List<OneSubject> getAllOneTwoSubject() {
//    //1 查询所有一级分类  parentid = 0
//    QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
//    wrapperOne.eq("parent_id","0");
//    List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
//
//    //2 查询所有二级分类  parentid != 0
//    QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
//    wrapperTwo.ne("parent_id","0");
//    List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);
//
//    //创建list集合，用于存储最终封装数据
//    List<OneSubject> finalSubjectList = new ArrayList<>();
//
//    //3 封装一级分类
//    //查询出来所有的一级分类list集合遍历，得到每个一级分类对象，获取每个一级分类对象值，
//    //封装到要求的list集合里面 List<OneSubject> finalSubjectList
//    for (int i = 0; i < oneSubjectList.size(); i++) { //遍历oneSubjectList集合
//        //得到oneSubjectList每个eduSubject对象
//        EduSubject eduSubject = oneSubjectList.get(i);
//        //把eduSubject里面值获取出来，放到OneSubject对象里面
//        OneSubject oneSubject = new OneSubject();
////            oneSubject.setId(eduSubject.getId());
////            oneSubject.setTitle(eduSubject.getTitle());
//        //eduSubject值复制到对应oneSubject对象里面
//        BeanUtils.copyProperties(eduSubject,oneSubject);
//        //多个OneSubject放到finalSubjectList里面
//        finalSubjectList.add(oneSubject);
//
//        //在一级分类循环遍历查询所有的二级分类
//        //创建list集合封装每个一级分类的二级分类
//        List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
//        //遍历二级分类list集合
//        for (int m = 0; m < twoSubjectList.size(); m++) {
//            //获取每个二级分类
//            EduSubject tSubject = twoSubjectList.get(m);
//            //判断二级分类parentid和一级分类id是否一样
//            if(tSubject.getParentId().equals(eduSubject.getId())) {
//                //把tSubject值复制到TwoSubject里面，放到twoFinalSubjectList里面
//                TwoSubject twoSubject = new TwoSubject();
//                BeanUtils.copyProperties(tSubject,twoSubject);
//                twoFinalSubjectList.add(twoSubject);
//                System.out.println(twoFinalSubjectList);
//            }
//        }
//        //把一级下面所有二级分类放到一级分类里面
//        oneSubject.setChildren(twoFinalSubjectList);
//    }
//    return finalSubjectList;
//}
}
