重构BeanDefinition-理解SINGLETON && PROTOTYPE
==

## 思考?

### 1. 为什么 beanDefinition中有parentName？这是一种什么样的数据结构?

### 2. ObjectFactory 和 BeanFactory的区别是什么?
`ObjectFactory`从源代码上来看,是用来生成各种bean对象的工厂,对工厂的角色有明确的分工,如专门生产单例的`SingletonFactory`
封装`ObjectFactory`是为了在`DefaultSingletonBeanRegistry`这样类中，调用`getSingleton`方法时，内嵌ObjectFatory的接口，从而让外部定义的`ObjectFactory`符合单例构造模式化

### 3.具体的策略采用`CglibSubclassingInstantiationStrategy`来进行实例化，这样的好处在于？

## 步骤

### 1.初始化 
xmlBeanDefinitionReader中对XML文件进行解析

### 2.注册进AbstractBeanFactory
由documentReader读取xml所定义的各种bean，抽象成beanDefinition的类层次结构后，调用注册工厂进行注册

### 3.进行实例化操作
两种方法：
  1.通过PostProcessor进行预处理
  2.直接调用预处理接口，对所有bean进行处理操作(其中一个好处在于，可以在解析阶段把bean缓存进容器中，增加getBean的调用速度)

实例化操作 采用了 `InstantiationStrategy`策略

实现了关于继承式的参数初始化注入过程
主要是在`BeanWrapperImpl`实现继承式的参数注入