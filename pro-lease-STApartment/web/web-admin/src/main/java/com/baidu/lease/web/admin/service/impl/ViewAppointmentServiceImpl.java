package com.baidu.lease.web.admin.service.impl;

import com.baidu.lease.model.entity.ViewAppointment;
import com.baidu.lease.model.enums.AppointmentStatus;
import com.baidu.lease.web.admin.mapper.ViewAppointmentMapper;
import com.baidu.lease.web.admin.service.ViewAppointmentService;
import com.baidu.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.baidu.lease.web.admin.vo.appointment.AppointmentVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【view_appointment(预约看房信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ViewAppointmentServiceImpl extends ServiceImpl<ViewAppointmentMapper, ViewAppointment>
        implements ViewAppointmentService {

    @Autowired
    private ViewAppointmentMapper viewAppointmentMapper;

    @Override
    public IPage<AppointmentVo> pageAppointmentByQuery(IPage<AppointmentVo> page, AppointmentQueryVo queryVo) {
        IPage<AppointmentVo> page1 = viewAppointmentMapper.pageAppointmentByQuery(page, queryVo);
        return page1;
    }

    @Override
    public void updateStatusById(Long id, AppointmentStatus status) {
        this.update(new LambdaUpdateWrapper<ViewAppointment>().eq(ViewAppointment::getApartmentId,id).set(ViewAppointment::getAppointmentStatus,status));
    }
}




