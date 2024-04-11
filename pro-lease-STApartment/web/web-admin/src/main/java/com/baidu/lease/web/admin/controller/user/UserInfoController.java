package com.baidu.lease.web.admin.controller.user;


import com.baidu.lease.common.result.Result;
import com.baidu.lease.model.entity.UserInfo;
import com.baidu.lease.model.enums.BaseStatus;
import com.baidu.lease.web.admin.service.UserInfoService;
import com.baidu.lease.web.admin.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户信息管理")
@RestController
@RequestMapping("/admin/user")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "分页查询用户信息")
    @GetMapping("page")
    public Result<IPage<UserInfo>> pageUserInfo(@RequestParam long current, @RequestParam long size, UserInfoQueryVo queryVo) {
        IPage<UserInfo> page = Page.of(current, size);


        LambdaQueryWrapper<UserInfo> eq = new LambdaQueryWrapper<UserInfo>()
                .like(queryVo.getPhone() != null, UserInfo::getPhone, queryVo.getPhone());

        IPage<UserInfo> page1 = userInfoService.page(page, eq
                .eq(queryVo.getPhone() != null, UserInfo::getStatus, queryVo.getStatus()));
        return Result.ok(page1);
    }

    @Operation(summary = "根据用户id更新账号状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam BaseStatus status) {
        userInfoService.update(new LambdaUpdateWrapper<UserInfo>().eq(UserInfo::getId,id).set(UserInfo::getStatus,status));
        return Result.ok();
    }
}
