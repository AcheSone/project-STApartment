package com.baidu.lease.web.admin.controller.apartment;


import com.baidu.lease.common.result.Result;
import com.baidu.lease.model.entity.AttrKey;
import com.baidu.lease.model.entity.AttrValue;
import com.baidu.lease.web.admin.service.AttrKeyService;
import com.baidu.lease.web.admin.service.AttrValueService;
import com.baidu.lease.web.admin.vo.attr.AttrKeyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "房间属性管理")
@RestController
@RequestMapping("/admin/attr")


//8.1.1.5
//关于此属性模块的说明
// 增添新属性是一个整体的功能，如果用表和对象的关系来表述，则增添属性增添的是一个对象
// 而增添属性值增添的是列中的对象值，列可以有多个，但列中的值相对此列来说是唯一的
public class AttrController {

    @Autowired
    private AttrKeyService attrKeyService;
    private AttrValueService attrValueService;


    @Operation(summary = "新增或更新属性名称")
    @PostMapping("key/saveOrUpdate")
    public Result saveOrUpdateAttrKey(@RequestBody AttrKey attrKey) {
        attrKeyService.saveOrUpdate(attrKey);
        return Result.ok();
    }

    @Operation(summary = "新增或更新属性值")
    @PostMapping("value/saveOrUpdate")
    public Result saveOrUpdateAttrValue(@RequestBody AttrValue attrValue) {
        attrValueService.saveOrUpdate(attrValue);
        return Result.ok();
    }


    @Operation(summary = "查询全部属性名称和属性值列表")
    @GetMapping("list")
    public Result<List<AttrKeyVo>> listAttrInfo() {

        return Result.ok();
    }

    @Operation(summary = "根据id删除属性名称")
    @DeleteMapping("key/deleteById")
    public Result removeAttrKeyById(@RequestParam Long attrKeyId) {
        return Result.ok();
    }

    @Operation(summary = "根据id删除属性值")
    @DeleteMapping("value/deleteById")
    public Result removeAttrValueById(@RequestParam Long id) {
        return Result.ok();
    }

}
