package com.baidu.lease.web.admin.controller.apartment;


import com.baidu.lease.common.result.Result;
import com.baidu.lease.model.entity.ApartmentInfo;
import com.baidu.lease.model.enums.ReleaseStatus;
import com.baidu.lease.web.admin.mapper.ApartmentInfoMapper;
import com.baidu.lease.web.admin.service.ApartmentInfoService;
import com.baidu.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.baidu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.baidu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.baidu.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "公寓信息管理")
@RestController
@RequestMapping("/admin/apartment")
public class ApartmentController {

    @Autowired
    private ApartmentInfoService apartmentInfoService;





    @Operation(summary = "保存或更新公寓信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ApartmentSubmitVo apartmentSubmitVo) {
        apartmentInfoService.saveOrUpdateApartment(apartmentSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询公寓列表")
    @GetMapping("pageItem")
    public Result<IPage<ApartmentItemVo>> pageItem(@RequestParam long current, @RequestParam long size, ApartmentQueryVo queryVo) {
        //ApartmentItemVo，是空闲房间的逻辑实体类，继承了ApartmentInfo，意味着是以前者数据类型作为返回值
        IPage<ApartmentItemVo> page = new Page<>(current,size);
        IPage<ApartmentItemVo> ipage = apartmentInfoService.pageApartmentItemByQuery(page,queryVo);
        return Result.ok(ipage);
    }

    @Operation(summary = "根据ID获取公寓详细信息")
    @GetMapping("getDetailById")
    public Result<ApartmentDetailVo> getDetailById(@RequestParam Long id) {
        ApartmentDetailVo apartmentDetailById = apartmentInfoService.getApartmentDetailById(id);
        return Result.ok(apartmentDetailById);
    }

    @Operation(summary = "根据id删除公寓信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        //删除公寓信息等同删除其下所有的标签图片配套等信息，所以不只要删除apartment_info中的信息，还要删除其他关联表的信息
        apartmentInfoService.removeApartmentById(id);
        return Result.ok();
    }

    @Operation(summary = "根据id修改公寓发布状态")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status) {
        apartmentInfoService.updateReleaseStatusById(id,status);
        return Result.ok();
    }

    @Operation(summary = "根据区县id查询公寓信息列表")
    @GetMapping("listInfoByDistrictId")
    public Result<List<ApartmentInfo>> listInfoByDistrictId(@RequestParam Long id) {
        List<ApartmentInfo> list = apartmentInfoService.list(new LambdaQueryWrapper<ApartmentInfo>().eq(ApartmentInfo::getDistrictId, id));
        return Result.ok(list);
    }
}














