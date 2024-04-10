package com.baidu.lease.web.admin.service;

import com.baidu.lease.model.entity.ApartmentInfo;
import com.baidu.lease.model.entity.RoomInfo;
import com.baidu.lease.model.enums.ReleaseStatus;
import com.baidu.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.baidu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.baidu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.baidu.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author liubo
* @description 针对表【apartment_info(公寓信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface ApartmentInfoService extends IService<ApartmentInfo> {
        void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo);

    IPage<ApartmentItemVo> pageApartmentItemByQuery(IPage<ApartmentItemVo> page,ApartmentQueryVo queryVo);

    ApartmentDetailVo getApartmentDetailById(Long id);

    void removeApartmentById(Long id);

    void updateReleaseStatusById(Long id, ReleaseStatus status);


}
