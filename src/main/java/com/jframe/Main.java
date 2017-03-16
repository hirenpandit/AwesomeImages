package com.jframe;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.config.SpringConfiguration;
import com.jframe.util.DownloadImages;

public class Main {

	public static void main(String args[]) {

		ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
		DownloadImages bean = context.getBean(DownloadImages.class);
		bean.initView();
	}

}
