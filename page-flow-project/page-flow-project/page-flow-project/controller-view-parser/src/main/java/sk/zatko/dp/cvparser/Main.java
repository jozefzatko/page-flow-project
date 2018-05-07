package sk.zatko.dp.cvparser;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sk.zatko.dp.cvparser.config.AppConfig;

/**
 * Application entry point
 *
 * @author Jozef Zatko
 */
public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ControllersParser controllersParser = context.getBean("controllersParser", ControllersParser.class);
        controllersParser.parseAndStoreApplicationControllers();

        ViewsParser viewsParser = context.getBean("viewsParser", ViewsParser.class);
        viewsParser.parseAndStoreApplicationViews();
    }
}
