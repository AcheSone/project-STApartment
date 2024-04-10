package com.baidu.lease.web.admin.service.impl;

import com.baidu.lease.common.exception.LeaseException;
import com.baidu.lease.common.result.ResultCodeEnum;
import com.baidu.lease.model.entity.*;
import com.baidu.lease.model.enums.ItemType;
import com.baidu.lease.model.enums.ReleaseStatus;
import com.baidu.lease.web.admin.mapper.*;
import com.baidu.lease.web.admin.service.*;
import com.baidu.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.baidu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.baidu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.baidu.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baidu.lease.web.admin.vo.fee.FeeValueVo;
import com.baidu.lease.web.admin.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    //因为mapper层的接口实现了BaseMapper接口，然后service层的接口和方法又间接实现和继承了BaseMapper中的方法，所以service层实现的增删改查都是间接使用BaseMapper中的
    @Autowired
    private GraphInfoService graphInfoService;

    @Autowired
    private ApartmentFacilityService apartmentFacilityService;

    @Autowired
    private ApartmentLabelService apartmentLabelService;

    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;

    @Autowired
    private RoomInfoService roomInfoService;

    @Override
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {

        Long submitVoId = apartmentSubmitVo.getId();

        /*1.用流的方法进行操作
        2.先进行删除操作，插入之前将原有相同公寓信息数据进行删除再重新插入，则可实现更新效果，

        3.先厘清表和类的结构，Vo的类继承了原实体类中的属性，是原实体类中逻辑数据的延伸类，
          可看做是针对某个功能所需数据而创造的合集拓展类。

        4.除了ApartmentSubmitVo中的集合属性，其他的属性通过saveOrUpdate可进行映射。*/

        //------------------------------------------------------------------
        //第一步，先进行判断用户上传的apartment数据是否为空
        boolean flat = apartmentSubmitVo.getId() != null && apartmentSubmitVo.getId() != 0;

        //第二步，对其他apartment表基础字段进行映射，若是存在，则进行更改覆盖，若是不存在，则进行插入操作
        super.saveOrUpdate(apartmentSubmitVo);

        //第三步，当集合字段数据不为空（存在）且不为零时，进行删除
        if (flat) {
            //Vo展示的是对一或者对多的关系，实体类中表现为集合，是与这个功能相关才封装到一起的,所以vo相当于中间表的实体类，而中间表的实体类，通常存储的都是两张多对多表对应的id值
            //因为ApartmentSubmitVo继承了ApartmentInfo，所以公寓的id值就是和其他集合（ApartmentSubmitVo中间表）关联的id值，也就是对多关系

            //1.删除图片,通过匿名内部类的方式，记得写泛型，根据中间表的对应图片id，和图片表的对应id，删除图片
            graphInfoService.remove(new LambdaQueryWrapper<GraphInfo>().eq(GraphInfo::getId, submitVoId).eq(GraphInfo::getItemType, ItemType.APARTMENT));
            //2.删除杂费
            apartmentFeeValueService.remove(new LambdaQueryWrapper<ApartmentFeeValue>().eq(ApartmentFeeValue::getApartmentId, submitVoId));
            //3.删除公寓标签
            apartmentLabelService.remove(new LambdaQueryWrapper<ApartmentLabel>().eq(ApartmentLabel::getApartmentId, submitVoId));
            //4.删除配套
            apartmentFacilityService.remove(new LambdaQueryWrapper<ApartmentFacility>().eq(ApartmentFacility::getApartmentId, submitVoId));
        }


        //第四步，如果是第一次进行插入，则跳过第三步，进行插入操作

        /*
        * 使用流对数组进行操作的解读
        * 1.通过Stream获取此集合的流
        * 2.通过map，将此集合分解成一个个的流对象，在map集合中进行操作，等于循环对每个集合对象进行操作
        * 3.书写map中的逻辑，map会根据逻辑将前面转换成的流，再构造成一个新的流
        * 4.通过foreach终结流，将形成的新集合保存到数据库
        * */


            //1.进行图片新增,GraphVo属性类里有两个自带字段，一个是名称，一个是url，其他的是继承字段
        /*List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        List<GraphInfo> graphInfoList = new ArrayList<>();
        for (GraphVo graphVo : graphVoList) {
            GraphInfo graphInfo = new GraphInfo();
            graphInfo.setUrl(graphVo.getUrl());
            graphInfo.setName(graphVo.getName());
            graphInfo.setItemType(ItemType.APARTMENT);
            graphInfo.setItemId(submitVoId);
            graphInfoList.add(graphInfo);
        }

        graphInfoService.saveBatch(graphInfoList);*/

        apartmentSubmitVo.getGraphVoList().stream().map(graphVo -> {
            GraphInfo graphInfo = new GraphInfo();
            graphInfo.setId(submitVoId);
            graphInfo.setUrl(graphVo.getUrl());
            graphInfo.setName(graphVo.getName());
            graphInfo.setItemType(ItemType.APARTMENT);
            return graphInfo;
        }).forEach(graphInfoService::saveOrUpdate);

        //2.进行标签新增，
        apartmentSubmitVo.getLabelIds().stream().map(labelId->new ApartmentLabel(submitVoId,labelId)).forEach(apartmentLabelService::saveOrUpdate);
            //3.进行配套新增
        apartmentSubmitVo.getFacilityInfoIds().stream().map(facilityId -> new ApartmentFacility(submitVoId,facilityId)).forEach(apartmentFacilityService::saveOrUpdate);
            //4.进行杂费新增
        apartmentSubmitVo.getFeeValueIds().stream().map(feevalueId-> new ApartmentFeeValue(submitVoId,feevalueId)).forEach(apartmentFeeValueService::saveOrUpdate);
    }




    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;

    @Autowired
    private FeeValueMapper feeValueMapper;

    @Autowired
    private LabelInfoMapper labelInfoMapper;



    @Override
    //与此方法功能相关的表为apartment_info,room_info,lease_agreement
    public IPage<ApartmentItemVo> pageApartmentItemByQuery(IPage<ApartmentItemVo> page, ApartmentQueryVo queryVo) {

        return apartmentInfoMapper.pageApartmentItemByQuery(page,queryVo);
    }

    @Override
    public ApartmentDetailVo getApartmentDetailById(Long id) {

        //为防止空指针异常，所以先判断查询到的信息是否存在
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
        if (apartmentInfo == null){
            return null;
        }

        //下面通过自定义查询语句，将查询到的结果集，封装到vo对象，并且返回给controller
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo,apartmentDetailVo);

        //------------------------------------------------------------
        /*List<GraphVo> graphVoList = new ArrayList<>();
        List<GraphInfo> graphInfoList = graphInfoService.list(new LambdaQueryWrapper<GraphInfo>().eq(GraphInfo::getItemId, id).eq(GraphInfo::getItemType, ItemType.APARTMENT));
        for (GraphInfo graphInfo : graphInfoList) {
            GraphVo graphVo1 = new GraphVo();
            graphVo1.setName(graphInfo.getName());
            graphVo1.setUrl(graphInfo.getUrl());
            graphVoList.add(graphVo1);
        }*/
        //以下是使用stream流的方法进行操作，查询所有的图片合集
        List<GraphVo> graphVoList = graphInfoService.list(new LambdaQueryWrapper<GraphInfo>().eq(GraphInfo::getItemId, id).eq(GraphInfo::getItemType, ItemType.APARTMENT))
                .stream().map(graphInfo ->
                        new GraphVo(graphInfo.getName(), graphInfo.getUrl())).toList();


        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentId(id);

        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);



        List<FeeValueVo> feeValueVoList = feeValueMapper.selectListByApartmentId(id);

        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(labelInfoList);

        apartmentDetailVo.setFeeValueVoList(feeValueVoList);

        return apartmentDetailVo;
    }

    //

    //===================================================
    @Override
    public void removeApartmentById(Long id) {
        //根据id进行单条的插入记录删除，若是公寓下存在房间信息，则不能删除，此时应该抛出异常
        LambdaQueryWrapper<RoomInfo> roomInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomInfoLambdaQueryWrapper.eq(RoomInfo::getApartmentId,id);
        long count = roomInfoService.count(roomInfoLambdaQueryWrapper);
        if (count<0){
            throw new LeaseException(ResultCodeEnum.DELETE_ERROR);
        }

        apartmentInfoMapper.deleteById(id);
        graphInfoService.remove(new LambdaUpdateWrapper<GraphInfo>().eq(GraphInfo::getItemId,id).eq(GraphInfo::getItemType,ItemType.APARTMENT));
        apartmentFacilityService.remove(new LambdaUpdateWrapper<ApartmentFacility>().eq(ApartmentFacility::getApartmentId,id));
        apartmentLabelService.remove(new LambdaUpdateWrapper<ApartmentLabel>().eq(ApartmentLabel::getApartmentId,id));
        apartmentFeeValueService.remove(new LambdaUpdateWrapper<ApartmentFeeValue>().eq(ApartmentFeeValue::getApartmentId,id));

    }

    @Override
    public void updateReleaseStatusById(Long id, ReleaseStatus status) {
        this.update(new LambdaUpdateWrapper<ApartmentInfo>().eq(ApartmentInfo::getId,id).set(ApartmentInfo::getIsRelease,status));
    }


}





