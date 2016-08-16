重构BeanDefinition-理解SINGLETON && PROTOTYPE
====

## 思考?

### 1. 为什么 beanDefinition中有parentName？这是一种什么样的数据结构?

### 2. ObjectFactory 和 BeanFactory的区别是什么?
`ObjectFactory`从源代码上来看,是用来生成各种bean对象的工厂,对工厂的角色有明确的分工,如专门生产单例的`SingletonFactory`
封装`ObjectFactory`是为了在`DefaultSingletonBeanRegistry`这样类中，调用`getSingleton`方法时，内嵌ObjectFatory的接口，从而让外部定义的`ObjectFactory`符合单例构造模式化

### 3.具体的策略采用`CglibSubclassingInstantiationStrategy`来进行实例化，这样的好处在于？

### 4.字符串expression解析框架，所采用的一些算法和原理?

### 5.组合和继承的一些实现差异的思考?
1.组合 需要对组合的域进行初始化功能,当然有些域是可配置的
2.继承 在子类继承父类后,父类的方法便可以直接使用,无需初始化父类操作

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

#### 3.1 bean实例化三步骤之生成beanInstance
#### 3.2 bean实例化三步骤之convertForProperty
beanInstance中所有的域进行实例化操作,借助`BeanWrapperImpl`中convertForProperty方法实现,并把处理权交给`TypeConverterDelegate`来实现。
针对不同的属性:获得属性的的值,以及属性对应的类型,如果属性与对应的类型相一致,则直接返回,否则就进行类型转换.
`PropertyEditorRegistry`是用来注册spring自身的classEditor来完成属性value的转换 
`CachedIntrospectionResults`的作用是? 能够得到某个beanClass所有的属性值,以及属性所对应的读or写方法

### 4.实例化可分为两种bean 和 factoryBean
当遇到FactoryBean进行实例化时,需要调用`invokeInitMethods()`来对`FactoryBean`进行初始化域，通过对Bean接口的判断来进行自身的回调操作
主要思想可以考虑一下：spring用来生成bean，同时bean自身定义了许多回调接口，再spring对bean进行初始化操作时，让bean对自己进行初始化操作。传统的new对象，需要对象自己显示的调用初始化方法，通过反射我们便能让这一切自动进行.

