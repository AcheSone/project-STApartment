关于Controller层的难点，传入标签数据时，要进行两次转换 

1标签管理LabelController类，所谓的标签管理，

            (若是传递参数，则通过SpringMVC中的WebDataBinder实现，而WebDataBinder依赖于接口Converter)
            (若是传递的是请求体，则通过SpringMVC的HTTPMessageConverter实现，@JsonValue注解)
            第一步.就是前端传输一个代表着某个属性的int数值到后端
            第二步，后端负责将这个int数值，转换成对应的枚举类对象
            
            第三步，后端在转换完成后，在Controller进行接收
            第四步，后端通过方法，对数据库进行CRUD

            (通过Mybatis中的TypeHandler实现，@EnumValue注解)
            第五步，后端在传入数据库之前，将枚举类转换成对应的int类型
            第六步，数据库将int类型的标签数据持久化到表中

            (通过SpringMVC的HTTPMessageConverter实现，@JsonValue注解)
            第七步，后端将返回值对象转换成JSON字符串，在此期间，
            将枚举类对象转换为对应的数字字符

2为什么要进行标签管理？为什么标签管理涉及到枚举类对象？
    是这样的

        首先是枚举，
            标签在项目中是客观存在且数量固定的，比如说，房间和公寓都是房子的标签，
            代表了两种不尽相同的房子类型，且在此项目中，我们仅关注这两种，所以它是
            固定的。在用户角度，他筛选房子种类时，会进行标签的增减以求寻找到符合自
            己意愿的房子，所以，这也是用户需求决定的。
        其次是为什么要进行标签管理，
            对前后端来说，约定传输int字符数据会比传输普通字符串要靠谱的多，用户不
            知晓后端逻辑，所以在编辑标签时，为了防止错误的输入而导致数据污染，所以
            使用int加枚举的方式来识别数据，保证了数据的正确性和完整性。

3.关于前端传后端、后端传数据库、后端传前端之间的类型转换

    ·前端后端传参（请求参数）转换比较复杂，要实现Converter转换器，mybatis-plus提供的
    ·后端传数据库转换使用@EnumValue注解注释标签枚举类即可，mybatis提供的
    ·前端后端相互传递请求体参数，使用@JsonValue注解修饰注释标签枚举类即可，SpringMVC提供的。