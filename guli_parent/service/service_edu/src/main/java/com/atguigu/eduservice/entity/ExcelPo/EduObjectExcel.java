package com.atguigu.eduservice.entity.ExcelPo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class EduObjectExcel {

    @ExcelProperty(index = 0)  // 代表在excel所在的列
    private String oneObjectName;

    @ExcelProperty(index = 1)  //在第二列
    private String twoObjectName;

}
