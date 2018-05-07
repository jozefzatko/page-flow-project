package sk.zatko.dp.pfv_gen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import sk.zatko.dp.pfv_gen.PageFlowViewsGen;
import sk.zatko.dp.pfv_gen.generator.SinglePageFlowViewGenService;
import sk.zatko.dp.pfv_gen.generator.SinglePageFlowViewGenServiceJavaMarkdownImpl;

/**
 * Application entry point
 *
 * @author Jozef Zatko
 */
@Configuration
@ComponentScan(basePackages = {"sk.zatko.dp.*"})
@PropertySources({
		@PropertySource("classpath:global.properties"),
		@PropertySource("config.properties")
})
public class AppConfig {

	@Bean
	PageFlowViewsGen pageFlowViewsGen() {
		return new PageFlowViewsGen();
	}

	@Bean
	SinglePageFlowViewGenService singlePageFlowViewGenService() {
		return new SinglePageFlowViewGenServiceJavaMarkdownImpl();
	}
}
