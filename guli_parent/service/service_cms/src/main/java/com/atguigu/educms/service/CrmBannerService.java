package com.atguigu.educms.service;

import com.atguigu.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-10
 */
public interface CrmBannerService extends IService<CrmBanner> {
    //    轮播图的查询所有
    List<CrmBanner> selectAllInfo();
}
