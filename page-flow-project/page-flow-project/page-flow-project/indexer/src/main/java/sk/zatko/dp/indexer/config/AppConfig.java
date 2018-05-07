package sk.zatko.dp.indexer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import sk.zatko.dp.indexer.Indexer;

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
    public Indexer indexer() {
        return new Indexer();
    }
}
