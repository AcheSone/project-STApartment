package com.baidu.crud.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("age")
    private Integer age;

    @TableField("email")
    private String email;

    //逻辑删除字段，作用说人话就是将该条用户的数据隐藏起来，此字段是为了防止后续公司和用户发生矛盾时的保证依据，是数据恢复的重要手段
    //另一个方面，也就是说，通过改变该值（1逻辑删除，0不删除）以逻辑层面而非物理层面进行删除
    @TableLogic
    private Integer deleted;

    public User(Long id){
        this.id = id;
    }
}
