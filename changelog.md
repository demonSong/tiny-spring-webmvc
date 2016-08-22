理解spring-hessian HTTP远程调用实现原理
====

## 思考?


## 步骤

### 1.在hessian中多了`HessianProxyFactory`这样一个工厂来生成hessianProxy的用意是什么？

`HessianProxyFactory`封装了STUBS建立过程的细节
 --1. 对生成两种代理对象的理解
   --1.1. spring层次的代理对象的封装
   --1.2. hessian层次的代理对象的封装:对服务方法及其参数进行序列化-进行hessian定义的binary-rpc编码操作
`Hessian`



