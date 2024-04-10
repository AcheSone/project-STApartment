package com.baidu.lease.web.admin.mapper;

import com.baidu.lease.model.entity.ApartmentInfo;
import com.baidu.lease.model.entity.RoomInfo;
import com.baidu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.baidu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* @author liubo
* @description 针对表【apartment_info(公寓信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.baidu.lease.model.ApartmentInfo
*/
public interface ApartmentInfoMapper extends BaseMapper<ApartmentInfo> {

    IPage<ApartmentItemVo> pageApartmentItemByQuery(IPage<ApartmentItemVo> page , ApartmentQueryVo queryVo);

    ApartmentInfo selectApartmentInfo(Long id);

    List<RoomInfo> selectRoomListToApartmentId(Long id);
}




