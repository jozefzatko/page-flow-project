package sk.zatko.dp.indexer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import sk.zatko.dp.indexer.indexing.IndexService;
import sk.zatko.dp.indexer.indexing.IndexServiceImpl;

/**
 * Spring test config
 *
 * @author Jozef Zatko
 */
@Configuration
@ComponentScan(basePackages = {"sk.zatko.dp.*"})
public class TestConfig {

	@Bean
	public IndexService indexService() {
		return new IndexServiceImpl();
	}
}

