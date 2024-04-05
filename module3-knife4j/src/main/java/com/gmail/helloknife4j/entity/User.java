package com.gmail.helloknife4j.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
//该注解是Knife4j用于描述作为接口参数或者返回值的 ’实体类‘ 的数据类型
@Schema(description = "用户信息实体")
public class User {

    @Schema(description = "编号")
    private Long id;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "用户年龄")
    private Integer age;

    @Schema(description = "用户邮箱")
    private String email;
}
