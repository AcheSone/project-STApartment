mybatis-plus关于注解的说明

    @TableName
    用作表名注解，用作标注表对应的实体类，value属性为要操作的表名称

    @TableId
    用作主键注解，表示表中对应的实体类字段的主键项

    @TableField
    用作非主键字段的标注，属性包含声明主键生成策略（也就是是否自增等）

    @TableLogic
    用作表明逻辑删除的字段标注，通过全局配置可实现不用手动注解也可逻辑删除，前提是属性名称与配置中的名称一致
    mybatis-plus:
        global-config:
             db-config:
                 logic-delete-field: deleted # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
                 logic-delete-value: 1 # 逻辑已删除值(默认为 1)
                 logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)

mybatis-plus的debug日志打印配置

    mybatis-plus:
    configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    logging:
    level:
    com.atguigu.mapper: debug