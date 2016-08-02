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

如何实现mvc 初始化后 xml文件和class文件的同时加载

### 3.step3-完成annotation进行ioc注解

下一步计划：根据autowire来完成自动实例化，而不需要getBean方法来获取实例