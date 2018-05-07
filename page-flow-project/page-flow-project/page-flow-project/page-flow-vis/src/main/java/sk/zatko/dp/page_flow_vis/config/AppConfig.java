package sk.zatko.dp.page_flow_vis.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Spring application config
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
}
