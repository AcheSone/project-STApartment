着重注意类和接口：
        枚举基类BaseEnum
        实体基类BaseEntity
        前端数字字符-枚举类，转换工厂类，StringToBaseEnumConverterFactory



关于此项目要学习了解的包结构

        VO：value object 值对象 / view object 表现层对象
        DTO（TO）：Data Transfer Object 数据传输对象
        DO：Domain Object 领域对象，就是从现实世界中抽象出来的有形或无形的业务实体。
        PO：persistent object 持久对象
        POJO ：plain ordinary java object 无规则简单java对象
        BO：business object 业务对象
        DAO：data access object数据访问对象
        
        以下是网页搜索的解释
        工作模型：
        用户发出请求（可能是填写表单），表单的数据在展示层被匹配为VO。
        展示层把VO转换为服务层对应方法所要求的DTO，传送给服务层。
        服务层首先根据DTO的数据构造（或重建）一个DO，调用DO的业务方法完成具体业务。
        服务层把DO转换为持久层对应的PO（可以使用ORM工具，也可以不用），调用持久层的持久化方法，把PO传递给它，完成持久化操作。
        对于一个逆向操作，如读取数据，也是用类似的方式转换和传递，略