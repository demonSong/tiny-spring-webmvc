package com.demon.springframework.web;

import org.junit.Test;

import demon.springframework.context.ApplicationContext;
import demon.springframework.context.ClassPathXmlApplicationContext;
import demon.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

public class SimpleUrlHandlerMappingTest {

	@Test
    public void test() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        SimpleUrlHandlerMapping simpleUrlHandlerMapping=(SimpleUrlHandlerMapping) applicationContext.getBean("urlMapping");
    }
}
