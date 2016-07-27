package com.demon.springframework.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import demon.springframework.context.ApplicationContext;
import demon.springframework.context.ClassPathXmlApplicationContext;
import demon.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

public class SimpleUrlHandlerMappingTest {

	@Test
    public void test() throws Exception {
		Map<String, Object> maps =new HashMap<String, Object>();
		System.out.println(maps.getClass().getInterfaces());
		
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        SimpleUrlHandlerMapping simpleUrlHandlerMapping=(SimpleUrlHandlerMapping) applicationContext.getBean("urlMapping");
    }
}
