实现AOPProxy,基于切面进行方法增强
====

## 思考?

### 1.接口使用小技巧,每个接口理想情况会有一个Support来支持,但在最顶层的类,会继续声明这样的接口,是为了可读性.让代码阅读这一眼看出所使用的接口.

### 2.在rmi通信中大量使用了proxy和aop技术!为何需要proxy来动态生成接口?

### 3.通过网络远程传来的类是什么样的形式呢?

### 4.`advised`接口和`advisor`接口以及`pointcut`接口的各自用途和区别是什么?

### 5.`TruePointcut`在AOP中是用来做什么的?

### 6.在`AopProxyUtils`中为什么需要添加两个SpringPox和Advised接口?

### 7.`InvocationHandler`invoke方法调用小细节
如果没有对method进行区分的话,代理对象每调用一个方法都回invoke一次,所以AOP面向切面需要进行切点的抽象,为了方便寻找出需要增强的方法

### 8.`JdkDynamicAopProxy`注意该类的invoke解耦思想,非常霸气

### 9.反射 `ProxyFactory`封装`TargetSource`生成的原因?
在对接口进行反射调用时,是没法进行多态调用的?即没法映射到对应的实体类?由反射性质所决定的
在动态加载过程中,即我们要指定某个接口对应的实体类,传入TargetSource

## 步骤

### 1.生成代理对象过程

需要获取代理对象的代理接口，这便是通过XML文件定义的接口，也是rmi约定的远程服务调用接口.
衍生的问题是：为什么需要动态代理来增强这样的一个接口？

所以说RMI的客户端基本思路是:
1.通过`rmiProxyFactoryBean`获得需要远程调用的接口类`serviceInterface`
2.由xml文件读取`serviceInterface`的具体接口定义，并进行接口Class转换
3.由`JdkDynamicAopProxy`生成该接口的动态代理对象
4.动态代理对象进行接口方法回调时,对其回调的方法做增强处理
5.在`ReflectiveMethodInvocation`进行对拦截器方法的回调,即我们需要增强的内容.
6.很显然,我们需要增强的内容便是`stub`调用远程服务的方法.

