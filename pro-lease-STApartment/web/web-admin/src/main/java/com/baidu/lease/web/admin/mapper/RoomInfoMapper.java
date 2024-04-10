package com.baidu.lease.web.admin.mapper;

import com.baidu.lease.model.entity.RoomInfo;
import com.baidu.lease.web.admin.vo.room.RoomItemVo;
import com.baidu.lease.web.admin.vo.room.RoomQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.baidu.lease.model.RoomInfo
*/
public interface RoomInfoMapper extends BaseMapper<RoomInfo> {

    IPage<RoomItemVo> saveRoomInPage(IPage<RoomItemVo> page, RoomQueryVo queryVo);
}




