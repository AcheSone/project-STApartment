package com.baidu.lease.web.admin.service.impl;

import com.baidu.lease.model.entity.*;
import com.baidu.lease.model.enums.ItemType;
import com.baidu.lease.web.admin.mapper.ApartmentInfoMapper;
import com.baidu.lease.web.admin.service.*;
import com.baidu.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baidu.lease.web.admin.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.baidu.lease.model.entity.ApartmentFacility;

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

    @Autowired
    private GraphInfoService graphInfoService;
    @Autowired
    private ApartmentFacilityService apartmentFacilityService;
    @Autowired
    private ApartmentLabelService apartmentLabelService;
    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;
    @Override
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
        //因为这个操作是综合了插入和更新操作的，删除的思想为，当数据不为空时，进行更新操作
        // 并删除原有数据，数据为空时，进行插入操作。
        boolean flag = apartmentSubmitVo.getId()!=null? true : false;

        //当数据存在时，执行删除操作，然后重新插入，当数据不存在时，直接执行插入操作
        if (flag){
            //1.删除图片
            //2.删除配套列表
            //3.删除标签列表
            //4.删除杂费列表
            LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //当图片类型是公寓,且传入的id与数据库中图片id相同时，进行删除
            graphInfoLambdaQueryWrapper
                    .eq(GraphInfo::getItemType, ItemType.APARTMENT)
                    .eq(GraphInfo::getItemId,apartmentSubmitVo.getId());
            graphInfoService.remove(graphInfoLambdaQueryWrapper);
            //-----------------------------------------------------------
            LambdaQueryWrapper<ApartmentFacility> apartmentFacilityLambdaQueryWrapper =
                    new LambdaQueryWrapper<>();
            apartmentFacilityLambdaQueryWrapper.eq(ApartmentFacility::getApartmentId,apartmentSubmitVo.getId());
            apartmentFacilityService.remove(apartmentFacilityLambdaQueryWrapper);
            //-----------------------------------------------------------
            LambdaQueryWrapper<ApartmentLabel> apartmentLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentLabelLambdaQueryWrapper.eq(ApartmentLabel::getApartmentId,apartmentSubmitVo.getId());
            apartmentLabelService.remove(apartmentLabelLambdaQueryWrapper);
            //-----------------------------------------------------------
            LambdaQueryWrapper<ApartmentFeeValue> apartmentFeeValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentFeeValueLambdaQueryWrapper.eq(ApartmentFeeValue::getApartmentId,apartmentSubmitVo.getId());
            apartmentFeeValueService.remove(apartmentFeeValueLambdaQueryWrapper);
        }

        //==============================================================================
        //下面执行插入操作
        //1.图片插入时，判断用户上传的图片逻辑类vo列表是否为空，若非空，则进行逐个图片实体类属性的插入操作

        List<GraphVo> graphVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(graphVoList)){
            //若是ApartmentSubmitVo对象中的图片对象集合非空（前端传入的），通过获取每个图片对象的属性值，
            // 设置给图片实体类对象,最后集体设置进List集合，造就一个GraphInfo表实体类对象集合。
            //最后将图片对象的数据集合插入数据库

            ArrayList<GraphInfo> arrayList = new ArrayList<>();
            for (GraphVo graphVo : graphVoList) {
                //遍历Vo封装的属性集合，每次提供一个Vo对象，将其属性值取出，赋值给实体类对象
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setItemId(apartmentSubmitVo.getId());
                arrayList.add(graphInfo);
            }
            graphInfoService.saveBatch(arrayList);
        }
        //==============================================================================
        //2.配套插入
        List<Long> facilityInfoIdsList = apartmentSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityInfoIdsList)){
            ArrayList<ApartmentFacility>  apartmentFacilityArrayList= new ArrayList<>();
            for (Long aLong : facilityInfoIdsList) {
                ApartmentFacility apartmentFacility = new ApartmentFacility();
                apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
                apartmentFacility.setFacilityId(aLong);
                apartmentFacilityArrayList.add(apartmentFacility);
            }
            apartmentFacilityService.saveBatch(apartmentFacilityArrayList);
        }
        //==============================================================================
        //3.
    }
}




