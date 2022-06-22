package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/educms/banner")
public class BannerFrontController {
    @Autowired
    private CrmBannerService crmBannerService;

//    轮播图的查询所有
    @GetMapping("getAll")
    public R getAllInfo(){
        List<CrmBanner> list =  crmBannerService.selectAllInfo();
        return R.ok().data("list",list);
    }

}
