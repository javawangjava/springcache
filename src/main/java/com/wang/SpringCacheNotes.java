package com.wang;

public class SpringCacheNotes {

/*
*
* Spring Cache是一个框架，实现了基于注解的缓存功能，只需要简单地加一个注解，就能实现缓存功能，大大简化我们在业务中操作缓存的代码。
* Spring Cache只是提供了一层抽象，底层可以切换不同的cache实现。具体就是通过CacheManager接口来统一不同的缓存技术。
* CacheManager是Spring提供的各种缓存技术抽象接口。
*
*
* 针对不同的缓存技术需要实现不同的CacheManager：
*       CacheManager 描述
*       EhCacheCacheManager     使用EhCache作为缓存技术
*       GuavaCacheManager       使用Google的GuavaCache作为缓存技术
*       RedisCacheManager       使用Redis作为缓存技术
*
*
*   Spring Cache 注解
*       在SpringCache中提供了很多缓存操作的注解，常见的是以下的几个：
*           注解 说明
*           @EnableCaching  类注解，启动类上使用@EnableCaching代表当前项目开启缓存注解功能
*           @Cacheable      方法注解，在方法执行前spring先查看缓存中是否有数据，如果有数据，则直接返回缓存数据；若没有数据，调用方法并将方法返回值放到缓存中
*           @CachePut       方法注解，将方法的返回值放到缓存中
*           @CacheEvict     方法注解，将一条或多条数据从缓存中删除
*
*       在spring boot项目中，使用缓存技术只需在项目中导入相关缓存技术的依赖包，并在启动类上使用@EnableCaching开启缓存支持即可。
*       由于SpringCache的基本功能是Spring核心(spring-context)中提供的。
*
*           @CachePut注解：作用: 将方法返回值放入缓存。一般用于保存操作。
*               value: 缓存的名称, 每个缓存名称下面可以有很多key
*               key: 缓存的key ----------> 支持Spring的表达式语言SPEL语法
*
*               key的写法如下：
*                   #user.id : #user指的是方法形参的名称, id指的是user的id属性 , 也就是使用user的id属性作为key ;
*                   #user.name: #user指的是方法形参的名称, name指的是user的name属性 ,也就是使用user的name属性作为key ;
*                   #result.id : #result代表方法返回值，该表达式 代表以返回对象的id属性作为key ；
*                   #result.name : #result代表方法返回值，该表达式 代表以返回对象的name属性作为key ；
*
*
*           @CacheEvict注解:作用: 清理指定缓存。一般用于更新或者删除操作。
*               value: 缓存的名称，每个缓存名称下面可以有多个key
*               key: 缓存的key ----------> 支持Spring的表达式语言SPEL语法
*
*
*           @Cacheable注解: 作用: 在方法执行前，spring先查看缓存中是否有数据，如果有数据，则直接返回缓存数据；若没有数据，调用方法并将方法返回值放到缓存中。
*               value: 缓存的名称，每个缓存名称下面可以有多个key
*               key: 缓存的key ----------> 支持Spring的表达式语言SPEL语法
*               缓存非null值：
*                   在@Cacheable注解中，提供了两个属性分别为： condition， unless 。
*                   condition : 表示满足什么条件, 再进行缓存 ;
*                   unless : 表示满足条件则不缓存 ; 与上述的condition是反向的 ;
*
*   集成Redis
*       在使用上述默认的ConcurrentHashMap做缓存时，服务重启之后，之前缓存的数据就全部丢失了，操作起来并不友好。在项目中使用，我们会选择使用redis来做缓存，主要需要操作以下几步：
*               1). 导入Spring Cache和Redis相关maven坐标：
*                   <artifactId>spring-boot-starter-data-redis</artifactId>
*                   <artifactId>spring-boot-starter-cache</artifactId>
*               2). 在application.yml中配置缓存数据的过期时间：
*               3). 在启动类上加入@EnableCaching注解，开启缓存注解功能：
*               4). 在需要进行缓存处理的方法上添加对应的@Cacheable注解或者：
*
*   设置缓存的失效时间：在配置类中或者配置文件中设置缓存的失效时间。配置文件和配置类网络搜一下直接拿来修改后直接用。
*
* */

}
