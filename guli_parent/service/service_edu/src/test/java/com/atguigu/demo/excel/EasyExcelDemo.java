package com.atguigu.demo.excel;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EasyExcelDemo {


//    写入excel操作
    @Test
    public void easyExcelDemo(){
        String fileName = "D:\\web\\stu.xlsx";
        List<ExcelDemo> objects = new ArrayList<>();

        for (int i = 0; i < 10 ;i++) {
            ExcelDemo excelDemo = new ExcelDemo();
            excelDemo.setId(i);
            excelDemo.setName("lis" + i);
            objects.add(excelDemo);
        }

        EasyExcel.write(fileName,ExcelDemo.class).sheet("学生").doWrite(objects);
    }



//    读Excel 操作
    @Test
    public void readExcel(){
//        读取的地址
        String fileName =  "D:\\web\\stu.xlsx";

//      获取监听器的对象 ,
        EasyExcel.read(fileName,ExcelDemo.class,new ExcelListener()).sheet().doRead();

    }

}
