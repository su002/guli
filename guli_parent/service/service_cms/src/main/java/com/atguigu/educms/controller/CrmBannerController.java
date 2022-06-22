package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-10
 */
@CrossOrigin
@RestController
@RequestMapping("/educms/banner")
public class CrmBannerController {

    @Autowired
    private CrmBannerService crmBannerService;

//    首页查询全部数据, 并做分页
    @GetMapping("pageBanner/{page}/{limit}")
//    page 当前页
//    limit 每页显示条数
    public R pageHappler(@PathVariable("page")  Long page, @PathVariable("limit") Long limit){

//        使用mybatis 的分页插件
        Page<CrmBanner> crmBannerList = new Page<>(page,limit);
//        调用方法, 实现查询
        crmBannerService.page(crmBannerList,null);
//        每页显示数据的集合
        List<CrmBanner> records = crmBannerList.getRecords();
//         查询出总记录数
        long total = crmBannerList.getTotal();
        return R.ok().data("items",records).data("total",total);
    }

//    根据id查询
    @GetMapping("get/{id}")
    public R getById(@PathVariable("id") String id){
        CrmBanner crmBanner = crmBannerService.getById(id);

        return R.ok().data("item",crmBanner);
    }



//    添加
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        crmBannerService.save(crmBanner);
        return R.ok();
    }

//    修改
    @PostMapping("update")
    public R updateBanner(@RequestBody CrmBanner crmBanner){

        crmBannerService.updateById(crmBanner);
        return R.ok();
    }

//    删除
    @PostMapping("delete/{id}")
    public R deleteBanner(@PathVariable("id") String id) {
        crmBannerService.removeById(id);
        return R.ok();
    }

}

