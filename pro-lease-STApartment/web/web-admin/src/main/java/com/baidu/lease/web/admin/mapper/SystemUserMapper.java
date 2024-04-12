package com.baidu.lease.web.admin.mapper;

import com.baidu.lease.model.entity.SystemUser;
import com.baidu.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.baidu.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
* @author liubo
* @description 针对表【system_user(员工信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.baidu.lease.model.SystemUser
*/
public interface SystemUserMapper extends BaseMapper<SystemUser> {


    SystemUserItemVo getSystemUserById(Long id);

    IPage<SystemUserItemVo> pageSystemUserByQuery(IPage<SystemUserItemVo> page, SystemUserQueryVo queryVo);

}




