package sk.zatko.dp.logparser.config;

import org.springframework.context.annotation.*;
import sk.zatko.dp.logparser.AccessLogParser;
import sk.zatko.dp.logparser.parser.LogFileParserService;
import sk.zatko.dp.logparser.parser.LogFileParserServiceSimpleTextImpl;
import sk.zatko.dp.logparser.parser.LogItemParserService;
import sk.zatko.dp.logparser.parser.LogItemParserServiceApacheTomcatImpl;

/**
 * Spring application config
 */
@Configuration
@ComponentScan(basePackages = {"sk.zatko.dp.*"})
@PropertySources({
        @PropertySource("classpath:global.properties"),
        @PropertySource("config.properties")
})
public class AppConfig {

    @Bean
    AccessLogParser accessLogParser() {
        return new AccessLogParser();
    }

    @Bean
    LogFileParserService logFileParserService() {
        return new LogFileParserServiceSimpleTextImpl();
    }

    @Bean
    LogItemParserService logItemParserService() {
        return new LogItemParserServiceApacheTomcatImpl();
    }

}
