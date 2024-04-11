mybatis-plus是什么？

    1.mybatis的增强，不对mybatis功能做任何删减，增加了更多可靠的表操作功能
    2.mybatis-plus提供了分页插件，使得程序员不用再关心分页逻辑的实现
    3.mybatis-plus提供了通用的SQL方法，在处理单表操作时，更加简洁便利



使用通用SQL的前提：mybatis-plus提供的都是单表操作的通用方法，多表连接查询不适用。

引入mybatis-plus依赖
<dependency>
<groupId>com.baomidou</groupId>
<artifactId>mybatis-plus-boot-starter</artifactId>
<version>3.5.3.2</version>
</dependency>


此模块用作测试mybatis-plus的BaseMapper抽象类（通用mapper）自带的CRUD方法
涉及测试的包，pojo,mapper,
涉及测试的类，TestUserCRUD
涉及要使用的外部接口和类：BaseMapper(类)
让mapper接口继承该抽象类，相当于获得了里面通用的单表CRUD方法
---注意事项：

    ~1.继承BaseMapper时必须声明泛型为该接口对应的实体类对象。
    ~2.必须使用@TableName注释实体类，告诉mybatis该接口对应的数据库表和字段之间的联系
    ~3.必须使用@TableId注释实体类中，对应表中为主键的id属性，并且设置type属性为表中对应的自增规则

---------------------------------------------------------------------------------------------------------------------

同时，此模块还用作测试mybatis-plus的通用Service，通过继承IService抽象类即可
涉及测试的包，pojo,service
涉及测试的类，TestUserAutoService
涉及要使用的外部接口和类，IService(类)，ServiceImpl(类)
---注意事项：

    ~0.继承抽象类时要指定实体类对象的泛型！IService是<Service对应实体类>，而ServiceImpl则是<实体类对象mapper接口,Service对应实体类>
    ~1.实现IService抽象类要实现其中所有的抽象方法，（这操作有点抽象），所以我们继承IService的子类ServiceImpl即可
    ~2.实现的步骤有点抽象，顺序是，
        1.使用Service接口继承IService抽象类，因为Service接口本身是抽象的，也就不用实现其中的抽象方法
        2.新建Service接口的实现类，该实现类实现Service接口，并且继承ServiceImpl，这样就避免了在实现类重写IService抽象方法的尴尬局面
    ~3.上述操作完即可进行进行测试


--------------------------------------------------------------------------------------------------------------------

同同时，此模块还用做测试条件构造器
涉及测试的类，TestTwoWrapper
涉及测试的外部类和接口，ServiceImpl，QueryWrapper，UpdateWrapper
---注意事项：

    ~0.什么是条件构造器？条件构造器是Java类，是为mybatis-plus生成的SQL语句增添where条件匹配的功能类
    ~1.条件构造器有几种？两种，QueryWrapper是用作查询和删除操作的条件构造器，UpdateWrapper是用作修改的条件构造器
    ~2.条件构造器的适用范围？条件构造器只能用于BaseMapper生成的SQL语句，若是自己编写SQL，则需要手动进行where子句的添加

以上条件构造器还有LambdaQueryWrapper`和`LambdaUpdateWrapper`这两个结合了Lambda表达式的版本
给相关参数以Lambda表达式的形式传入即可，这样可以避免手动赋值导致的参数写死。

-------------------------------------------------------------------------------------------------------------------

同同同时，此模块还用做测试逻辑删除和mybatis-plus分页插件的使用

---注意事项：

    ·逻辑删除
    ~1.逻辑删除字段每张表都必须有，每个对应的实体类也是
    ~2.逻辑删除的字段，0代表删除，1代表未删除
    ~3.所谓逻辑删除也就是将该表的行数据进行屏蔽，此时若是通过任意形式执行BaseMapper的删除操作，都会转变成修改操作，
        即，将删除状态改为0，此后，所有的mybatis-plus查询操作都会追加一个where deleted ！= 0 的操作。（自编写老老实实自己加）。

    ·分页插件
    ~1.首先要先新建IPage类对象，该类是专门用作分页插件的类，通过其静态方法传入要分页的规则即可得到
    ~2.哪个方法需要就将对象传给谁呗
    ~3.分页插件只有开启MybatisPlusInterceptor拦截器之后才生效，拦截器，必须在注册之后，纳入IOC容器的管理
    ~4.mybatis-plus提供的分页插件