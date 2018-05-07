package sk.zatko.dp.identifier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import sk.zatko.dp.identifier.PageFlowIdentifier;
import sk.zatko.dp.identifier.bundles.RequestMappingService;
import sk.zatko.dp.identifier.bundles.RequestMappingServiceSpringMVCImpl;
import sk.zatko.dp.identifier.bundles.SplitBundleService;
import sk.zatko.dp.identifier.bundles.SplitBundleServiceTimeGapImpl;
import sk.zatko.dp.identifier.bundles.CreateBundlesService;
import sk.zatko.dp.identifier.bundles.CreateBundlesServiceImpl;
import sk.zatko.dp.identifier.filtering.AccessLogFilterService;
import sk.zatko.dp.identifier.filtering.AccessLogFilterServiceImpl;
import sk.zatko.dp.identifier.postprocessing.PageFlowsPostprocessingService;
import sk.zatko.dp.identifier.postprocessing.PageFlowsPostprocessingServiceImpl;
import sk.zatko.dp.identifier.seq_mining.PageFlowsMiningService;
import sk.zatko.dp.identifier.seq_mining.PageFlowsMiningServiceMGFSMImpl;

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
    PageFlowIdentifier pageFlowIdentifier() {
        return new PageFlowIdentifier();
    }

    @Bean
    AccessLogFilterService accessLogFilterService() {
        return new AccessLogFilterServiceImpl();
    }

    @Bean
    CreateBundlesService createBundlesService() {
        return new CreateBundlesServiceImpl();
    }

    @Bean
    RequestMappingService requestMappingService() {
        return new RequestMappingServiceSpringMVCImpl();
    }

    @Bean
    SplitBundleService splitBundleService() {
        return new SplitBundleServiceTimeGapImpl();
    }

    @Bean
    PageFlowsMiningService pageFlowsMiningService() {
        return new PageFlowsMiningServiceMGFSMImpl();
    }

    @Bean
    PageFlowsPostprocessingService pageFlowsPostprocessingService() {
        return new PageFlowsPostprocessingServiceImpl();
    }

}
