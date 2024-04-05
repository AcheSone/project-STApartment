package com.baidu.lease.web.admin.controller.apartment;


import com.baidu.lease.common.result.Result;
import com.baidu.lease.model.entity.PaymentType;
import com.baidu.lease.web.admin.service.PaymentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "支付方式管理")
@RequestMapping("/admin/payment")
@RestController
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService paymentTypeService;

    @Operation(summary = "查询全部支付方式列表")
    @GetMapping("list")
    public Result<List<PaymentType>> listPaymentType() {
        List<PaymentType> list = paymentTypeService.list();
        //成功查询要将结果返回前端
        return Result.ok(list);
    }

    @Operation(summary = "保存或更新支付方式")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdatePaymentType(@RequestBody PaymentType paymentType) {
        boolean saveOrUpdate = paymentTypeService.saveOrUpdate(paymentType);
        //保存和更新操作无需返回数据体给前端
        return Result.ok();
    }

    @Operation(summary = "根据ID删除支付方式")
    @DeleteMapping("deleteById")
    public Result deletePaymentById(@RequestParam Long id) {
        boolean removeById = paymentTypeService.removeById(id);
        //设置逻辑删除之后，这里的物理删除操作mybatis-plus会自动转换成更新操作，即，将删除状态码改为1.
        //和更新插入两个操作一样，删除也不需要返回数据实体，所以不传入Result响应对象中
        return Result.ok();
    }

}















