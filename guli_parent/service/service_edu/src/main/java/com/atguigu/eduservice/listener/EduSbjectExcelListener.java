package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.ExcelPo.EduObjectExcel;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


public class EduSbjectExcelListener extends AnalysisEventListener<EduObjectExcel> {

    /** 在该监听器中 , 不能交给spring容器创建对象并管理,
     *       不能直接在拿到数据
     *       后对数据库进行写入操作,
     *     因此需要对该类进行手动注入
     *          在该处使用构造器注入
     * @param eduObjectExcel
     * @param analysisContext
     */
    public EduSubjectService subjectService;


    public EduSbjectExcelListener() {}
    public EduSbjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }




//    一级分类插入操作 , 判断不能重复添加
    private EduSubject oneexistSubject(EduSubjectService subjectService ,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id" ,"0");  //一级菜单 parent_id 为 0
        EduSubject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }

//   二级分类  ,判断不能重复添加
    private EduSubject twoSubject(EduSubjectService subjectService ,String name, String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();

        wrapper.eq("title",name);
        wrapper.eq("parent_id" ,pid);  //二级菜单 parent_id pid 为传入的值
        EduSubject twoSubject = subjectService.getOne(wrapper);
        return twoSubject;
    }

//    读取上传的 Excel文件
    @Override
    public void invoke(EduObjectExcel eduObjectExcel, AnalysisContext analysisContext) {
        if (eduObjectExcel == null) {
            try {
                throw new GuliException(20001, "上传文件数据为空");
            } catch (GuliException e) {
                e.printStackTrace();
            }
        }
//        在读取时 , 是一行一行读取 ,每次读取两个值, 第一个为一级分类  ,第二个为二级分类
//        需要判断一级 分类是否重复

        EduSubject oneeduSubject = this.oneexistSubject(subjectService, eduObjectExcel.getOneObjectName());
//        如果 oneeduSubject 的为null 则表示数据库没有 ,则对其进行添加
        if (oneeduSubject == null) {
            oneeduSubject = new EduSubject();
//           建立联系 , 添加
            oneeduSubject.setParentId("0");
            oneeduSubject.setTitle(eduObjectExcel.getOneObjectName());
            subjectService.save(oneeduSubject);
        }


//        二级分类的的与一级分类的id 相同
//        获取 一级分类id的值 , 作为二级分类的id
        String pid = oneeduSubject.getId();

        //添加二级分类 ,判断数据库是否有重复
        EduSubject twoSubject = this.twoSubject(subjectService, eduObjectExcel.getTwoObjectName(), pid);
        if (twoSubject == null){
            twoSubject = new EduSubject();
            twoSubject.setParentId(pid  );
            twoSubject.setTitle(eduObjectExcel.getOneObjectName());
            subjectService.save(twoSubject);
        }

    }

//    读取完文件之后执行的方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
