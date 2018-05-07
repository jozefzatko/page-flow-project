package sk.zatko.dp.cvparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import sk.zatko.dp.cvparser.ControllersParser;
import sk.zatko.dp.cvparser.ViewsParser;
import sk.zatko.dp.cvparser.controllers.ControllerParserService;
import sk.zatko.dp.cvparser.controllers.ControllerParserServiceSpringMVCImpl;
import sk.zatko.dp.cvparser.layout.PageLayoutService;
import sk.zatko.dp.cvparser.layout.PageLayoutServiceJSPApacheTilesImpl;
import sk.zatko.dp.cvparser.views.TilesConfigParser;
import sk.zatko.dp.cvparser.views.TilesConfigParserImpl;
import sk.zatko.dp.cvparser.views.ViewParserService;
import sk.zatko.dp.cvparser.views.ViewParserServiceJSPApacheTilesImpl;

@Configuration
@ComponentScan(basePackages = {"sk.zatko.dp.*"})
@PropertySources({
        @PropertySource("classpath:global.properties"),
        @PropertySource("config.properties")
})
public class AppConfig {

    @Bean
    ControllersParser controllersParser() {
        return new ControllersParser();
    }

    @Bean
    ControllerParserService controllerParserService() {
        return new ControllerParserServiceSpringMVCImpl();
    }

    @Bean
    PageLayoutService pageLayoutService() {
        return new PageLayoutServiceJSPApacheTilesImpl();
    }

    @Bean
    ViewsParser viewsParser() {
        return new ViewsParser();
    }

    @Bean
    ViewParserService viewParserService() {
        return new ViewParserServiceJSPApacheTilesImpl();
    }

    @Bean
    TilesConfigParser tilesConfigParser() {
        return new TilesConfigParserImpl();
    }

}