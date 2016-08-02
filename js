[1mdiff --git a/README.md b/README.md[m
[1mindex a3522c7..ceacccf 100644[m
[1m--- a/README.md[m
[1m+++ b/README.md[m
[36m@@ -19,4 +19,6 @@[m [mtiny-spring-webmvc[m
 [m
 下一步计划,把配置文件指定在默认的/WEB-INFO/文件夹下[m
 [m
[31m-实现ioc注解方式的实现[m
\ No newline at end of file[m
[32m+[m[32m实现ioc注解方式的实现[m
[32m+[m
[32m+[m[32m如何实现mvc 初始化后 xml文件和class文件的同时加载[m
\ No newline at end of file[m
[1mdiff --git a/changelog.md b/changelog.md[m
[1mindex 2904e39..737531e 100644[m
[1m--- a/changelog.md[m
[1m+++ b/changelog.md[m
[36m@@ -301,6 +301,7 @@[m [mtiny-spring-webmvc[m
 2.两者组成了java访问文件的根目录，最终用递归方法，遍历所有符合的class[m
 3.对class文件进行解读？但显然还需要封装一些基本的类来完成上述操作[m
 4.spring中,采取asm读取字节码的方式来读取class中的基本配置信息在`SimpleMetadataReader`中进行实现,并由`visitor`完成对classReader的访问[m
[32m+[m[32m5.实现自己的注解[m
 [m
 [m
 [m
[1mdiff --git a/src/main/java/demon/springframework/context/annotation/ClassPathScanningCandidateComponentProvider.java b/src/main/java/demon/springframework/context/annotation/ClassPathScanningCandidateComponentProvider.java[m
[1mindex f6cceb1..220c176 100644[m
[1m--- a/src/main/java/demon/springframework/context/annotation/ClassPathScanningCandidateComponentProvider.java[m
[1m+++ b/src/main/java/demon/springframework/context/annotation/ClassPathScanningCandidateComponentProvider.java[m
[36m@@ -8,7 +8,6 @@[m [mimport java.util.List;[m
 import java.util.Set;[m
 [m
 import org.springframework.core.io.support.ResourcePatternResolver;[m
[31m-import org.springframework.stereotype.Component;[m
 import org.springframework.util.ClassUtils;[m
 import org.springframework.util.SystemPropertyUtils;[m
 [m
[36m@@ -21,6 +20,7 @@[m [mimport demon.springframework.core.type.classreading.MetadataReaderFactory;[m
 import demon.springframework.core.type.classreading.SimpleMetadataReaderFactory;[m
 import demon.springframework.core.type.filter.AnnotationTypeFilter;[m
 import demon.springframework.core.type.filter.TypeFilter;[m
[32m+[m[32mimport demon.springframework.stereotype.Component;[m
 [m
 public class ClassPathScanningCandidateComponentProvider {[m
 	[m
[1mdiff --git a/src/main/java/demon/springframework/web/servlet/HelloWorldService.java b/src/main/java/demon/springframework/web/servlet/HelloWorldService.java[m
[1mdeleted file mode 100644[m
[1mindex 4c915df..0000000[m
[1m--- a/src/main/java/demon/springframework/web/servlet/HelloWorldService.java[m
[1m+++ /dev/null[m
[36m@@ -1,9 +0,0 @@[m
[31m-package demon.springframework.web.servlet;[m
[31m-[m
[31m-/**[m
[31m- * @author yihua.huang@dianping.com[m
[31m- */[m
[31m-public interface HelloWorldService {[m
[31m-[m
[31m-    void helloWorld();[m
[31m-}[m
[1mdiff --git a/src/main/java/demon/springframework/web/servlet/HelloWorldServiceImpl.java b/src/main/java/demon/springframework/web/servlet/HelloWorldServiceImpl.java[m
[1mdeleted file mode 100644[m
[1mindex 3762a54..0000000[m
[1m--- a/src/main/java/demon/springframework/web/servlet/HelloWorldServiceImpl.java[m
[1m+++ /dev/null[m
[36m@@ -1,25 +0,0 @@[m
[31m-package demon.springframework.web.servlet;[m
[31m-[m
[31m-/**[m
[31m- * @author yihua.huang@dianping.com[m
[31m- */[m
[31m-public class HelloWorldServiceImpl implements HelloWorldService {[m
[31m-[m
[31m-    private String text;[m
[31m-[m
[31m-    private OutputService outputService;[m
[31m-[m
[31m-    @Override[m
[31m-    public void helloWorld(){[m
[31m-        outputService.output(text);[m
[31m-    }[m
[31m-[m
[31m-    public void setText(String text) {[m
[31m-        this.text = text;[m
[31m-    }[m
[31m-[m
[31m-    public void setOutputService(OutputService outputService) {[m
[31m-        this.outputService = outputService;[m
[31m-    }[m
[31m-[m
[31m-}[m
[1mdiff --git a/src/main/java/demon/springframework/web/servlet/OutputService.java b/src/main/java/demon/springframework/web/servlet/OutputService.java[m
[1mdeleted file mode 100644[m
[1mindex 35f9d78..0000000[m
[1m--- a/src/main/java/demon/springframework/web/servlet/OutputService.java[m
[1m+++ /dev/null[m
[36m@@ -1,8 +0,0 @@[m
[31m-package demon.springframework.web.servlet;[m
[31m-[m
[31m-/**[m
[31m- * @author yihua.huang@dianping.com[m
[31m- */[m
[31m-public interface OutputService {[m
[31m-    void output(String text);[m
[31m-}[m
[1mdiff --git a/src/main/java/demon/springframework/web/servlet/OutputServiceImpl.java b/src/main/java/demon/springframework/web/servlet/OutputServiceImpl.java[m
[1mdeleted file mode 100644[m
[1mindex a0b5acd..0000000[m
[1m--- a/src/main/java/demon/springframework/web/servlet/OutputServiceImpl.java[m
[1m+++ /dev/null[m
[36m@@ -1,13 +0,0 @@[m
[31m-package demon.springframework.web.servlet;[m
[31m-[m
[31m-/**[m
[31m- * @author yihua.huang@dianping.com[m
[31m- */[m
[31m-public class OutputServiceImpl implements OutputService {[m
[31m-[m
[31m-    @Override[m
[31m-    public void output(String text){[m
[31m-        System.out.println(text);[m
[31m-    }[m
[31m-[m
[31m-}[m
[1mdiff --git a/src/main/resources/tinyioc.xml b/src/main/resources/tinyioc.xml[m
[1mdeleted file mode 100644[m
[1mindex ba62821..0000000[m
[1m--- a/src/main/resources/tinyioc.xml[m
[1m+++ /dev/null[m
[36m@@ -1,16 +0,0 @@[m
[31m-<?xml version="1.0" encoding="UTF-8"?>[m
[31m-<beans xmlns="http://www.springframework.org/schema/beans"[m
[31m-	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"[m
[31m-	xmlns:tx="http://www.springframework.org/schema/tx" xmlns:context="http://www.springframework.org/schema/context"[m
[31m-	xsi:schemaLocation="[m
[31m-	http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd[m
[31m-	http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd[m
[31m-	http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd[m
[31m-	http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd">[m
[31m-[m
[31m-[m
[31m-	<!--controller 映射关系的建立 -->[m
[31m-	<bean id="add" class="demon.springframework.web.servlet.test.AddController"></bean>[m
[31m-	<bean id="update" class="demon.springframework.web.servlet.test.UpdateController"></bean>[m
[31m-[m
[31m-</beans>[m
\ No newline at end of file[m
[1mdiff --git a/src/test/java/com/demon/springframework/web/SimpleUrlHandlerMappingTest.java b/src/test/java/com/demon/springframework/web/SimpleUrlHandlerMappingTest.java[m
[1mdeleted file mode 100644[m
[1mindex 6593c2b..0000000[m
[1m--- a/src/test/java/com/demon/springframework/web/SimpleUrlHandlerMappingTest.java[m
[1m+++ /dev/null[m
[36m@@ -1,22 +0,0 @@[m
[31m-package com.demon.springframework.web;[m
[31m-[m
[31m-import java.util.HashMap;[m
[31m-import java.util.Map;[m
[31m-[m
[31m-import org.junit.Test;[m
[31m-[m
[31m-import demon.springframework.context.ApplicationContext;[m
[31m-import demon.springframework.context.ClassPathXmlApplicationContext;[m
[31m-import demon.springframework.web.servlet.handler.SimpleUrlHandlerMapping;[m
[31m-[m
[31m-public class SimpleUrlHandlerMappingTest {[m
[31m-[m
[31m-	@Test[m
[31m-    public void test() throws Exception {[m
[31m-		Map<String, Object> maps =new HashMap<String, Object>();[m
[31m-		System.out.println(maps.getClass().getInterfaces());[m
[31m-		[m
[31m-        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");[m
[31m-        SimpleUrlHandlerMapping simpleUrlHandlerMapping=(SimpleUrlHandlerMapping) applicationContext.getBean("urlMapping");[m
[31m-    }[m
[31m-}[m
[1mdiff --git a/src/test/java/demon/springframework/b