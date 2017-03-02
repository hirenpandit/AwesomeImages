package com.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = { "com" })
public class SpringBeanConfiguration {

	// @Bean
	// public DownloadBingImages getDownloadBingImages() {
	// return new DownloadBingImages();
	// }

}
