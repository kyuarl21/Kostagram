package kyu.pj.kostagram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer { //Web 설정 파일
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		// file: /Users/kyu/Coding/IdeaProjects/Kostagram/src/main/resources/static/upload/
		registry
			.addResourceHandler("/upload/**") //jsp페이지에서 이런 주소패턴이 나오면 uploadFolder가 발동
			.addResourceLocations("file:///" + uploadFolder)
			.setCachePeriod(60 * 10 * 6) //1시간
			.resourceChain(true)
			.addResolver(new PathResourceResolver());
	}
}
