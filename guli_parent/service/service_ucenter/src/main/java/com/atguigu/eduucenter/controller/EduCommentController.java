package com.atguigu.eduucenter.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.jwtUtils.JwtUtils;
import com.atguigu.eduucenter.client.EduClientService;
import com.atguigu.eduucenter.entity.EduComment;
import com.atguigu.eduucenter.entity.UcenterMember;
import com.atguigu.eduucenter.service.EduCommentService;
import com.atguigu.eduucenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-15
 */
@RestController
@RequestMapping("/eduucenter/comment")
@CrossOrigin
public class EduCommentController {
    @Autowired
    private EduCommentService eduCommentService;
    @Autowired
    private EduClientService eduClientService;
    @Autowired
    private UcenterMemberService ucenterMemberService;

//    分页查询课程评论方法
    @GetMapping("getAllPage/{page}/{limit}")
    public R getAllPage(@PathVariable Long page,@PathVariable Long limit){
        Page<EduComment> commentPage = new Page<>(page,limit);

       eduCommentService.page(commentPage, null);
        List<EduComment> records = commentPage.getRecords();

        long total = commentPage.getTotal();
        return R.ok().data("total",total).data("records",records);
    }





//    课程评论的实现

    @GetMapping("getComment/{id}")
    public R getComment(@PathVariable String id,
                        HttpServletRequest request
                        ){
//        根据id查询 课程详情 , 讲师id
        R courseById = eduClientService.getCourseById(id);
        if (courseById.getCode() == 20001) {
            throw new GuliException(20001,"执行了熔断器");
        }
//        获取请求头的用户id
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember user = ucenterMemberService.getById(userId);

        return R.ok().data("user",user);

    }
}

