package com.demon.springframework.web;

import org.junit.Test;

import demon.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import us.codecraft.tinyioc.context.ApplicationContext;
import us.codecraft.tinyioc.context.ClassPathXmlApplicationContext;

public class SimpleUrlHandlerMappingTest {

	@Test
    public void test() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        SimpleUrlHandlerMapping simpleUrlHandlerMapping=(SimpleUrlHandlerMapping) applicationContext.getBean("urlMapping");
    }
}
