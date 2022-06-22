package com.atguigu.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class ExcelListener extends AnalysisEventListener<ExcelDemo> {
    @Override
    public void invoke(ExcelDemo excelDemo, AnalysisContext analysisContext) {
        System.out.println("读取的数据:"+ excelDemo);
    }

//    读取表头
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头:"+ headMap);

    }

    //    读取完成之后执行的方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println(11);
    }
}
