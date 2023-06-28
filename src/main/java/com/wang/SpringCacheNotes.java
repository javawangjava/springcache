package com.wang;

public class SpringCacheNotes {

/*
*
* 需要手写操作缓存代码，如添加缓存、更新缓存、删除缓存。
* 切换缓存组件并不容易，或者说没有对缓存层进行抽象封装，依赖具体的缓存中间件。
* Spring Cache 是 Spring 提供的一整套的缓存解决方案。虽然它本身并没有提供缓存的实现，但是它提供了一整套的接口和代码规范、配置、注解等，这样它就可以整合各种缓存方案了，比如 Redis、Ehcache，我们也就不用关心操作缓存的细节。
* Spring Cache是一个框架，实现了基于注解的缓存功能，只需要简单地加一个注解，就能实现缓存功能，大大简化我们在业务中操作缓存的代码。
* Spring Cache只是提供了一层抽象，底层可以切换不同的cache实现。具体就是通过CacheManager接口来统一不同的缓存技术。
* CacheManager是Spring提供的各种缓存技术抽象接口。
*
*
* Spring Cache 有什么功效
*   每次调用某方法，而此方法又是带有缓存功能时，Spring 框架就会检查指定参数的那个方法是否已经被调用过，如果之前调用过，就从缓存中取之前调用的结果；
*   如果没有调用过，则再调用一次这个方法，并缓存结果，然后再返回结果，那下次调用这个方法时，就可以直接从缓存中获取结果了。
*
*
* Spring Cache 的原理是什么？
*  Spring Cache 主要是作用在类上或者方法上，对类中的方法的返回结果进行缓存。那么如何对方法增强，来实现缓存的功能？
*  学过 Spring 的同学，肯定能一下子就反应过来，就是用AOP（面向切面编程）。
*  面向切面编程可以简单地理解为在类上或者方法前加一些说明，就是我们常说的注解。
*  Spring Cache 的注解会帮忙在方法上创建一个切面（aspect），并触发缓存注解的切点（poinitcut），听起来太绕了。
* 简单点说就是：Spring Cache 的注解会帮忙在调用方法之后，去缓存方法调用的最终结果，或者在方法调用之前拿缓存中的结果，或者删除缓存中的结果，这些读、写、删缓存的脏活都交给 Spring Cache 来做了，是不是很爽，再也不用自己去写缓存操作的逻辑了。


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
*           @CachePut       方法注解，在方法调用前不会去缓存中找，@CachePut 注解的方法始终都会执行，返回值也会也会放到缓存中。通常用在保存的方法上。
*           @CacheEvict     方法注解，将一条或多条数据从缓存中删除。
*           @Caching        方法注解，能够同时应用多个缓存注解。
*           @CacheConfig    方法注解，在类级别共享相同的缓存的配置。
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
*               Cacheable 注解中，可以添加四种参数：value，key，condition，unless。
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
