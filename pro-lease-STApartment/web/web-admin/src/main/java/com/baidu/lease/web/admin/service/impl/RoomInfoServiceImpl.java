package com.baidu.lease.web.admin.service.impl;

import com.baidu.lease.model.entity.*;
import com.baidu.lease.model.enums.ItemType;
import com.baidu.lease.web.admin.mapper.RoomInfoMapper;
import com.baidu.lease.web.admin.service.*;
import com.baidu.lease.web.admin.vo.attr.AttrKeyVo;
import com.baidu.lease.web.admin.vo.attr.AttrValueVo;
import com.baidu.lease.web.admin.vo.graph.GraphVo;
import com.baidu.lease.web.admin.vo.room.RoomDetailVo;
import com.baidu.lease.web.admin.vo.room.RoomItemVo;
import com.baidu.lease.web.admin.vo.room.RoomQueryVo;
import com.baidu.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service

public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {


    @Autowired
    private GraphInfoService graphInfoService;
    @Autowired
    private RoomAttrValueService roomAttrValueService;

    @Autowired
    private RoomFacilityService roomFacilityService;

    @Autowired
    private RoomLabelService roomLabelService;

    @Autowired
    private RoomPaymentTypeService roomPaymentTypeService;

    @Autowired
    private RoomLeaseTermService roomLeaseTermService;


    //注意房间表跟其他表进行的关联的依据是表主键ID，而不是房间号ID

    @Override
    public void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo) {
        Long Id = roomSubmitVo.getId();
        if (Id != null) {
            //进行逻辑删除
            this.removeById(Id);

            //多对多时仅需删除中间表的数据即可，因为删除后，两张表再无关系，而找准中间表则很关键，此项目中，中间表的命名通常以左表和右边相结合而命名

            graphInfoService.remove(new LambdaUpdateWrapper<GraphInfo>().eq(GraphInfo::getItemId, roomSubmitVo.getApartmentId()).eq(GraphInfo::getItemType, ItemType.ROOM));

            roomAttrValueService.remove(new LambdaUpdateWrapper<RoomAttrValue>().eq(RoomAttrValue::getRoomId, Id));

            roomFacilityService.remove(new LambdaUpdateWrapper<RoomFacility>().eq(RoomFacility::getRoomId, Id));

            roomLabelService.remove(new LambdaUpdateWrapper<RoomLabel>().eq(RoomLabel::getRoomId, Id));

            roomLeaseTermService.remove(new LambdaUpdateWrapper<RoomLeaseTerm>().eq(RoomLeaseTerm::getRoomId, Id));

            roomPaymentTypeService.remove(new LambdaUpdateWrapper<RoomPaymentType>().eq(RoomPaymentType::getRoomId, Id));


        }

        //保存公寓基本信息
        RoomInfo roomInfo = new RoomInfo();
        BeanUtils.copyProperties(roomSubmitVo, roomInfo);
        this.save(roomInfo);

        //保存房间图片列表
        roomSubmitVo.getGraphVoList().stream()
                .map(graphVo -> new GraphInfo(graphVo.getName(), ItemType.ROOM, Id, graphVo.getUrl())).forEach(graphInfoService::save);

        //保存房间属性信息列表
        roomSubmitVo.getAttrValueIds().stream().map(aLong -> new RoomAttrValue(Id, aLong)).forEach(roomAttrValueService::save);

        //保存房间配套信息列表
        roomSubmitVo.getFacilityInfoIds().stream().map(aLong -> new RoomFacility(Id, aLong)).forEach(roomFacilityService::save);

        //保存房间标签信息列表
        roomSubmitVo.getLabelInfoIds().stream().map(aLong -> new RoomLabel(Id, aLong)).forEach(roomLabelService::save);

        //保存房间支付方式
        roomSubmitVo.getPaymentTypeIds().stream().map(aLong -> RoomPaymentType.builder().roomId(Id).paymentTypeId(aLong).build()).forEach(roomPaymentTypeService::save);

        //保存房间可选租期方式
        roomSubmitVo.getLeaseTermIds().stream().map(aLong -> RoomLeaseTerm.builder().roomId(Id).leaseTermId(aLong).build()).forEach(roomLeaseTermService::save);
    }

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Override
    public IPage<RoomItemVo> saveRoomInPage(IPage<RoomItemVo> page, RoomQueryVo queryVo) {
        //手动指定查询条件，通过SQL语句对RoomItemVo字段属性进行自动填充,page只管传入即可，只要注册了拦截器，mybatis-plus就会根据page对象进行分页配置自动加上Limit
        //前提是，mapper接口中的方法一定是要以IPage<要查询的类型>作为返回值

        //多提一句，queryVo作为的是查询条件，根据用户选择的地点，作为查询语句的条件依据
        IPage<RoomItemVo> page1 = roomInfoMapper.saveRoomInPage(page, queryVo);
        return page1;
    }

    @Override
    //根据ID对公寓房间信息进行查询
    public RoomDetailVo selectRoomDetaiVoById(Long id) {
        return null;
    }

}


