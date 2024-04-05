package com.baidu.lease.web.admin.controller.apartment;


import com.baidu.lease.common.result.Result;
import com.baidu.lease.model.entity.LabelInfo;
import com.baidu.lease.model.enums.ItemType;
import com.baidu.lease.web.admin.service.LabelInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "标签管理")
@RestController
@RequestMapping("/admin/label")
public class LabelController {

    @Autowired
    private LabelInfoService labelInfoService;

    @Operation(summary = "（根据类型）查询标签列表")
    @GetMapping("list")
    public Result<List<LabelInfo>> labelList(@RequestParam(required = false) ItemType type) {
        //为了获取type中代表类型的int属性，所以我们需要进行where子句的定制，
        // 所以就要用到mybatis-plus提供的条件构造器，这里推荐使用Lambda形式的，
        // 因为是查询，所以使用LambdaQueryWrapper，对比条件为type，

        LambdaQueryWrapper<LabelInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //这里使用了三参数的eq，先判断是否为空，若不为空，则进行枚举类对象也就是从前端int数据转换成枚举类的
        // 的值进行getType属性获取，这里的获取要通过转换器实现，所以还要去配置转换器
        lambdaQueryWrapper.eq(type != null,LabelInfo::getType,type);

        //最后使用service对象，并将条件构造器传入查询方法，为查询方法进行where子句的增添
        List<LabelInfo> infoList = labelInfoService.list(lambdaQueryWrapper);

        return Result.ok(infoList);
    }

    @Operation(summary = "新增或修改标签信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdateLabel(@RequestBody LabelInfo labelInfo) {
        labelInfoService.saveOrUpdate(labelInfo);
        return Result.ok();
    }

    @Operation(summary = "根据id删除标签信息")
    @DeleteMapping("deleteById")
    public Result deleteLabelById(@RequestParam Long id) {
        labelInfoService.removeById(id);
        return Result.ok();
    }
}
