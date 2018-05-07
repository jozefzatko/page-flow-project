package sk.zatko.dp.context_gen.config;

import org.springframework.context.annotation.*;
import sk.zatko.dp.context_gen.PageFlowContextGen;
import sk.zatko.dp.context_gen.controllers.AssignControllersService;
import sk.zatko.dp.context_gen.controllers.AssignControllersServiceImpl;
import sk.zatko.dp.context_gen.views.AssignViewsService;
import sk.zatko.dp.context_gen.views.AssignViewsServiceImpl;

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

	@Bean
	PageFlowContextGen pageFlowContextGen() {
		return new PageFlowContextGen();
	}

	@Bean
	AssignControllersService assignControllersService() {
		return new AssignControllersServiceImpl();
	}

	@Bean
	AssignViewsService assignViewsService() {
		return new AssignViewsServiceImpl();
	}
}
