tiny-spring-webmvc
=======

>A tiny IoC container and webmvc refer to Spring.

## 功能

1. 支持spring MVC框架 实现HTTP请求至Controller的分发

## 使用

`tiny-spring-webmvc`是逐步进行构建的，里程碑版本我都使用了git tag来管理。例如，最开始的tag是`step-1-spring-mvc-add-controller`，那么可以使用

	git checkout step-1-spring-mvc-add-controller

### 1.step1-实现mvc到controller的直接映射

### 2.step2-使用handlermapping来管理映射关系

下一步计划,把配置文件指定在默认的/WEB-INFO/文件夹下

实现ioc注解方式的实现

封装到classpath*beanFactory中去，从而理解servlet初始化过程的contextLoader

如何实现mvc 初始化后 xml文件和class文件的同时加载

### 3.step3-完成annotation进行ioc注解

下一步计划：根据autowire来完成自动实例化，而不需要getBean方法来获取实例
autowire实现基本思路:
有一个入口类来完成xml文件的配置读取工作,然后当在getBean获得某个类时,进行autowire扫描来实现依赖注入
autowire有两种实现方式:
1.通过beanPostProcessor在bean进行初始化时,对其进行xml注入,但由于有AUTOWIRE标识所以可以省去set方法
2.不需要任何处理器,也不需要编写定义任何xml数据,直接进行初始化,背后实现的机制是什么呢?比较适合无业务对象的逻辑,即没有基本类型的成员对象.

各种bean 在初始化的时候会调用一个beanAnnotationPostProcessor用来扫描所有field的注解,从而进行依赖注入

完成autowire初始化field功能

### 4.step4-完成对xml解析元素的封装和路由
使用`beanDefinitionParserDelegate`来实现对元素的不同操作,任务ok

### 5.step5-增加对RequestMapping的实现
实现对requestMapping 1.初始化注册 2.加载匹配 
获得handlermethod,最终通过反射来实现方法的调用
需要思考参数列表与web请求如何建立映射关系

### 6.step6-理解spring-rmi TCP远程调用实现原理
学习RmiProxyFactoryBean
首先需要搭建factoryBean,需要完成对默认基本configureBeanDefinition的一些参数的初始化
对bean进行xml解析时,注册是通过PostProcessorRegistrationDelegate这些处理器来完成的.

#### 6.1重构BeanDefinition-理解SINGLETON && PROTOTYPE(完成)

#### 6.2实现AOPProxy,基于切面进行方法增强

### 7.step7-理解spring-hessian HTTP远程调用实现原理

