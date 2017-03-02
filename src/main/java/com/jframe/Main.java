package com.jframe;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.config.SpringBeanConfiguration;

public class Main {

	public static void main(String args[]) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				SpringBeanConfiguration.class);
		MyApplication myApplication = context.getBean(MyApplication.class);
		myApplication.initializeView();

		context.close();

	}

}
