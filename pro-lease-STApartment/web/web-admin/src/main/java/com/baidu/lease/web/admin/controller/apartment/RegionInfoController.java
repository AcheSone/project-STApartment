package com.baidu.lease.web.admin.controller.apartment;


import com.baidu.lease.common.result.Result;
import com.baidu.lease.model.entity.CityInfo;
import com.baidu.lease.model.entity.DistrictInfo;
import com.baidu.lease.model.entity.ProvinceInfo;
import com.baidu.lease.web.admin.service.CityInfoService;
import com.baidu.lease.web.admin.service.DistrictInfoService;
import com.baidu.lease.web.admin.service.ProvinceInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "地区信息管理")
@RestController
@RequestMapping("/admin/region")
public class RegionInfoController {

    @Autowired
    private ProvinceInfoService provinceInfoService;

    @Autowired
    private CityInfoService cityInfoService;

    @Autowired
    private DistrictInfoService districtInfoService;

    //选了省份才能查询城市，选了城市才能查询区县

    @Operation(summary = "查询省份信息列表")
    @GetMapping("province/list")
    public Result<List<ProvinceInfo>> listProvince() {
        List<ProvinceInfo> list = provinceInfoService.list();
        return Result.ok(list);
    }

    @Operation(summary = "根据省份id查询城市信息列表")
    @GetMapping("city/listByProvinceId")
    public Result<List<CityInfo>> listCityInfoByProvinceId(@RequestParam Long id) {
        LambdaQueryWrapper<CityInfo> cityInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cityInfoLambdaQueryWrapper.eq(CityInfo::getProvinceId,id);
        //上面这段代码的意思是，将查询到的ProvinceId信息和传入的省份id进行比较，若是比较成功。则根据相应的省份id查询名下所有的城市
        List<CityInfo> list = cityInfoService.list(cityInfoLambdaQueryWrapper);
        return Result.ok(list);
    }

    @GetMapping("district/listByCityId")
    @Operation(summary = "根据城市id查询区县信息")
    public Result<List<DistrictInfo>> listDistrictInfoByCityId(@RequestParam Long id) {
        LambdaQueryWrapper<DistrictInfo> districtInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        districtInfoLambdaQueryWrapper.eq(DistrictInfo::getCityId,id);
        //上面这段代码的意思是，将查询到的CityId信息和传入的城市id进行比较，若是比较成功。则根据相应的城市id查询名下所有的区县
        List<DistrictInfo> list = districtInfoService.list(districtInfoLambdaQueryWrapper);
        return Result.ok(list);
    }

}
