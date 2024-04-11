package com.baidu.lease.web.admin.controller.system;


import com.baidu.lease.common.result.Result;
import com.baidu.lease.model.entity.SystemPost;
import com.baidu.lease.model.entity.SystemUser;
import com.baidu.lease.model.enums.BaseStatus;
import com.baidu.lease.web.admin.mapper.SystemPostMapper;
import com.baidu.lease.web.admin.service.SystemPostService;
import com.baidu.lease.web.admin.service.SystemUserService;
import com.baidu.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.baidu.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "后台用户信息管理")
@RestController
@RequestMapping("/admin/system/user")
public class SystemUserController {


    @Autowired
    private SystemUserService systemUserService;

    /*@Autowired
    private SystemPostMapper systemPostMapper;*/

    @Operation(summary = "根据条件分页查询后台用户列表")
    @GetMapping("page")
    public Result<IPage<SystemUserItemVo>> page(@RequestParam long current, @RequestParam long size, SystemUserQueryVo queryVo) {
        Page<SystemUserItemVo> page = Page.of(current, size);
        IPage<SystemUserItemVo> page1 = systemUserService.pageSystemUserByQuery(page, queryVo);
        return Result.ok(page1);
    }


        /*         IPage<SystemUserItemVo> page = Page.of(current, size);

        List<SystemUser> systemUserList = systemUserService.list();

        List<SystemUserItemVo> systemUserItemVoList = new ArrayList<>();

        for (int i = 0; i < systemUserList.size(); i++) {
            SystemUserItemVo systemUserItemVo1 = new SystemUserItemVo();
            BeanUtils.copyProperties(systemUserList.get(i),systemUserItemVo1);
            systemUserItemVoList.add(systemUserItemVo1);
        }

        List<Long> longs = systemUserList.stream().map(systemUser -> systemUser.getPostId()).toList();
        for (Long aLong : longs) {
            SystemPost systemPost = systemPostMapper.selectById(aLong);
            String name = systemPost.getName();
            Long id = systemPost.getId();
            for (SystemUserItemVo systemUserItemVo : systemUserItemVoList) {

                if (systemUserItemVo.getPostId() == id){
                    systemUserItemVo.setPostName(name);
                }
            }
        }
        page.setRecords(systemUserItemVoList);
        System.out.println(systemUserItemVoList);
*/


    @Operation(summary = "根据ID查询后台用户信息")
    @GetMapping("getById")
    public Result<SystemUserItemVo> getById(@RequestParam Long id) {
        SystemUserItemVo systemUserById = systemUserService.getSystemUserById(id);
        return Result.ok(systemUserById);
    }

    @Operation(summary = "保存或更新后台用户信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemUser systemUser) {
        //通过引入Commons的工具类，对密码进行加密处理，存入数据库的密码不会以明文的形式显示
        if (systemUser.getPassword() != null) {
            String s = DigestUtils.md5Hex(systemUser.getPassword());
            systemUser.setPassword(s);
        }
        systemUserService.saveOrUpdate(systemUser);
        return Result.ok();
    }

    @Operation(summary = "判断后台用户名是否可用")
    @GetMapping("isUserNameAvailable")
    public Result<Boolean> isUsernameExists(@RequestParam String username) {
        //查询后台用户列表，对比名称是否有相同的吗，若是可用，则返回true
        long count = systemUserService.count(new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUsername, username));
        if (count != 0) {
            return Result.ok(new Boolean(false));
        }
        return Result.ok(new Boolean(true));
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "根据ID删除后台用户信息")
    public Result removeById(@RequestParam Long id) {
        systemUserService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据ID修改后台用户状态")
    @PostMapping("updateStatusByUserId")
    public Result updateStatusByUserId(@RequestParam Long id, @RequestParam BaseStatus status) {
        systemUserService.update(new LambdaUpdateWrapper<SystemUser>().eq(SystemUser::getId, id).set(SystemUser::getStatus, status));
        return Result.ok();
    }
}
