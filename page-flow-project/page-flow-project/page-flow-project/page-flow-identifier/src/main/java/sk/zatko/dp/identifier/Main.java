package sk.zatko.dp.identifier;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sk.zatko.dp.identifier.config.AppConfig;
import sk.zatko.dp.identifier.PageFlowIdentifier;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        PageFlowIdentifier pageFlowIdentifier = context.getBean("pageFlowIdentifier", PageFlowIdentifier.class);
        pageFlowIdentifier.identifyAndStorePageFlows();
    }
}
