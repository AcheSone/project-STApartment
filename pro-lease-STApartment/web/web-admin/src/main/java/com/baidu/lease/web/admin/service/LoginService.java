package com.baidu.lease.web.admin.service;

import com.baidu.lease.web.admin.vo.login.CaptchaVo;
import com.baidu.lease.web.admin.vo.login.LoginVo;
import com.baidu.lease.web.admin.vo.system.user.SystemUserInfoVo;

public interface LoginService {

    CaptchaVo getCaptcha();
}
