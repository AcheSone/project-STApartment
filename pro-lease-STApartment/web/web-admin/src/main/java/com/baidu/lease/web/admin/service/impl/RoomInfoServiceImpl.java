package com.baidu.lease.web.admin.service.impl;

import com.baidu.lease.model.entity.*;
import com.baidu.lease.model.enums.ItemType;
import com.baidu.lease.web.admin.mapper.*;
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

//保存和删除操作一般使用mybatis自带API，即使用Service进行调用即可
//查询操作比较多样化，对一属性使用Service即可解决，对多要使用mapper编写查询语句
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
                .map(graphVo -> new GraphInfo(graphVo.getName(), ItemType.ROOM, Id, graphVo.getUrl()))
                .forEach(graphInfoService::save);

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

    @Autowired
    private AttrValueMapper attrValueMapper;

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;

    @Autowired
    private LabelInfoMapper labelInfoMapper;

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    @Autowired
    private LeaseTermMapper leaseTermMapper;


    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;



    @Override
    //根据房间ID对公寓房间信息进行查询
    public RoomDetailVo selectRoomDetaiVoById(Long id) {
        //根据房间ID查询房间基本信息，这里也一并能查询到房间所属的公寓ID
        RoomInfo roomInfo = roomInfoMapper.selectById(id);
        RoomDetailVo roomDetailVo = new RoomDetailVo();
        BeanUtils.copyProperties(roomInfo, roomDetailVo);
        //






//-------------------------------------------------------
        //以下操作都是一对多进行查询。
        //根据房间id查询图片列表，使用mybatis解决
        List<GraphVo> graphVoList = graphInfoService.list(new LambdaQueryWrapper<GraphInfo>()
                        .eq(GraphInfo::getItemId, id)
                        .eq(GraphInfo::getItemType, ItemType.ROOM))
                .stream().map(graphInfo -> GraphVo.builder().name(graphInfo.getName()).url(graphInfo.getUrl()).build()).toList();
        roomDetailVo.setGraphVoList(graphVoList);


        //根据房间id查询公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectApartmentInfo(id);
        roomDetailVo.setApartmentInfo(apartmentInfo);
        //因为是对一关系，也可以直接使用mybatis-plus提供的api进行操作，因为上面得到了房间所属公寓的id，所以这里可以引用
        //apartmentInfoService.list(new LambdaQueryWrapper<ApartmentInfo>().eq(ApartmentInfo::getId,roomInfo.getApartmentId())).stream().toList();



        //根据id查询属性信息列表，使用mapper文件解决
        List<AttrValueVo> attrValueVos = attrValueMapper.selectAttrValueVo(id);
        roomDetailVo.setAttrValueVoList(attrValueVos);

        //根据房间id查询其详细的配套信息列表
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectFacilityInfo(id);
        roomDetailVo.setFacilityInfoList(facilityInfoList);

        List<LabelInfo> labelInfoList = labelInfoMapper.selectLabelInfo(id);
        roomDetailVo.setLabelInfoList(labelInfoList);

        List<PaymentType> paymentTypes = paymentTypeMapper.selectPaymentType(id);
        roomDetailVo.setPaymentTypeList(paymentTypes);

        List<LeaseTerm> leaseTerms = leaseTermMapper.selectLeaseTerm(id);
        roomDetailVo.setLeaseTermList(leaseTerms);
        return roomDetailVo;
    }

    @Override
    //根据房间id删除房间信息
    public void delectRoomMessageById(Long id) {
        //1.删除房间基本信息
        //2.删除房间的中间表信息

        //删除房间表的房间信息
        this.removeById(id);
        //删除房间表关联的中间表信息和其他关联表信息，所以这是对一操作，可以使用mybatis-plus的API

        //删除图片
        graphInfoService.remove(new LambdaQueryWrapper<GraphInfo>().eq(GraphInfo::getItemType,ItemType.ROOM).eq(GraphInfo::getItemId,id));

        //删除属性信息列表
        roomAttrValueService.remove(new LambdaQueryWrapper<RoomAttrValue>().eq(RoomAttrValue::getRoomId,id));

        //删除配套列表信息
        roomFacilityService.remove(new LambdaQueryWrapper<RoomFacility>().eq(RoomFacility::getRoomId,id));

        roomLabelService.remove(new LambdaQueryWrapper<RoomLabel>().eq(RoomLabel::getRoomId,id));
        roomPaymentTypeService.remove(new LambdaQueryWrapper<RoomPaymentType>().eq(RoomPaymentType::getRoomId,id));
        roomLeaseTermService.remove(new LambdaQueryWrapper<RoomLeaseTerm>().eq(RoomLeaseTerm::getRoomId,id));

    }

    @Override
    //根据公寓id，查询房间列表
    public List<RoomInfo> selectRoomListToApartmentId(Long id) {
        List<RoomInfo> roomInfos = apartmentInfoMapper.selectRoomListToApartmentId(id);
        return roomInfos;
    }


}


