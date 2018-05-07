package sk.zatko.dp.logparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import sk.zatko.dp.logparser.parser.LogFileParserService;
import sk.zatko.dp.logparser.parser.LogFileParserServiceSimpleTextImpl;
import sk.zatko.dp.logparser.parser.LogItemParserService;
import sk.zatko.dp.logparser.parser.LogItemParserServiceApacheTomcatImpl;

/**
 * Spring test config
 *
 * @author Jozef Zatko
 */
@Configuration
@ComponentScan(basePackages = {"sk.zatko.dp.*"})
public class TestConfig {

	@Bean
	LogFileParserService logFileParserService() {
		return new LogFileParserServiceSimpleTextImpl();
	}

	@Bean
	LogItemParserService logItemParserService() {
		return new LogItemParserServiceApacheTomcatImpl();
	}
}
