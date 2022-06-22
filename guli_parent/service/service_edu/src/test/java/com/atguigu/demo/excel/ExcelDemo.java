package com.atguigu.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelDemo {
    @ExcelProperty(value = "编号" ,index = 0)
    private Integer id;
    @ExcelProperty(value = "姓名" ,index = 1)
    private String name;

}
