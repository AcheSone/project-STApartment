package com.baidu.lease.web.admin.controller.apartment;


import com.baidu.lease.common.result.Result;
import com.baidu.lease.model.entity.AttrKey;
import com.baidu.lease.model.entity.AttrValue;
import com.baidu.lease.web.admin.service.AttrKeyService;
import com.baidu.lease.web.admin.service.AttrValueService;
import com.baidu.lease.web.admin.vo.attr.AttrKeyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Autowired
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

        //实现方式一
        //1.先查询所有的名称
        //2.再迭代每一个名称，去查询对应的值列表


        //这里的思想很简单，因为Key和Value不是同一张表，而是通过KeyID联系起来的。所以用查询到的AttrKeyList所有的值，
        // 通过循环，一个一个进行查询并且将每一个Key对应的Id给予Value使用，将查询到的Value集合和Key值对应上，
        // 并设置进相应的逻辑合集vo对象集合，也就是纯使用mybatis-plus进行对多的操作。

        // 不建议使用此操作，更建议使用在mapper自编写select语句，因为频繁的查询操作会降低服务器性能。
        /*List<AttrKey> attrKeyList = attrKeyService.list();
        List<AttrKeyVo> attrKeyVoList = new ArrayList<>(attrKeyList.size());
        attrKeyList.forEach(attrKey -> {
            List<AttrValue> attrValueList = attrValueService.list(new LambdaQueryWrapper<AttrValue>()
            .eq(AttrValue::getAttrKeyId, attrKey.getId()));

            AttrKeyVo attrKeyVo = new AttrKeyVo();
            BeanUtils.copyProperties(attrKey,attrKeyVo);
            //attrKeyVo.setId(attrKey.getId());
            //attrKeyVo.setName(attrKey.getName());
            attrKeyVo.setAttrValueList(attrValueList);
            attrKeyVoList.add(attrKeyVo);
        });*/


        List<AttrKeyVo> attrKeyVos = attrKeyService.listAttrInfo();


        /*List<AttrKeyVo> attrKeyVoList = new ArrayList<>();
        List<AttrKey> attrKeyList = attrKeyService.list();
        attrKeyList.forEach(key -> {
                    LambdaQueryWrapper<AttrValue> attrValueLambdaQueryWrapper = new LambdaQueryWrapper<AttrValue>().eq(AttrValue::getAttrKeyId, key.getId());
                    List<AttrValue> list = attrValueService.list(attrValueLambdaQueryWrapper);

                    AttrKeyVo attrKeyVo = new AttrKeyVo();
                    attrKeyVo.setAttrValueList(list);
                    attrKeyVoList.add(attrKeyVo);
                }
        );*/
        return Result.ok(attrKeyVos);
    }

    @Operation(summary = "根据id删除属性名称")
    @DeleteMapping("key/deleteById")
    public Result removeAttrKeyById(@RequestParam Long attrKeyId) {
        attrKeyService.removeById(attrKeyId);
        //删除名称时，也就是一个大标签时，所有的小标签也应该被删除

        LambdaQueryWrapper<AttrValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //小标签的的字段依赖大标签的主键，所以根据大标签id删除
        lambdaQueryWrapper.eq(AttrValue::getAttrKeyId, attrKeyId);
        attrValueService.remove(lambdaQueryWrapper);

        return Result.ok();
    }

    @Operation(summary = "根据id删除属性值")
    @DeleteMapping("value/deleteById")
    public Result removeAttrValueById(@RequestParam Long id) {
        attrValueService.removeById(id);
        return Result.ok();
    }

}
