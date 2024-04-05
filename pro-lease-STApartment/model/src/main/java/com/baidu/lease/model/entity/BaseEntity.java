package com.baidu.lease.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
//此类充当了所有表的实现类的基类，即抽离了所有相同属性，又实现了序列化
//为何要实现序列化？因为这些属性都是要和Redis进行交互存储的。而Redis存储时，要求对象进行序列化
public class BaseEntity implements Serializable {

    //此类中有一个新注解
    //@JsonIgnore，表示在mybatis-plus进行CRUD时，智能进行忽略
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonIgnore
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    @JsonIgnore
    private Date updateTime;

    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    @JsonIgnore
    @TableLogic
    private Byte isDeleted;

}