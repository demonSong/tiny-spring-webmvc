理解spring-aop 实现原理
====

## 思考?

### 1.在xml中.获取service是具体的实际bean,spring是如何根据pointCut来判断完成代理的实现呢?

基于`InstantiationAwareBeanPostProcessor`这样一个前置处理器来判断,当前bean是否需要进行代理,即AOP

具体的proxy代理生成由`AnnotationAwareAspectJAutoProxyCreator`来完成.

所以说,spring在对bean进行初始化结束后,会有个postProcessor用来做一些bean的后续操作,这些操作中,其中就包含了对bean进行代理对象的生成.并且由代理对象覆盖原先的bean,从而使得客户端的调用为代理对象.

## 步骤





