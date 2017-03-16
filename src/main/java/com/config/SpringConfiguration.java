package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.jframe.DownloadBingImages;

@Configuration
@ComponentScan("com")
public class SpringConfiguration {
	
	@Bean
	public DownloadBingImages downloadBingImages(){
		return new DownloadBingImages();
	}

}
