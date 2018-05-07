package sk.zatko.dp.indexer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sk.zatko.dp.indexer.config.AppConfig;
import sk.zatko.dp.indexer.indexing.IndexService;

/**
 * Application entry point
 *
 * @author Jozef Zatko
 */
public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Indexer indexer = context.getBean("indexer", Indexer.class);
        indexer.createAndStoreIndex();
    }
}
