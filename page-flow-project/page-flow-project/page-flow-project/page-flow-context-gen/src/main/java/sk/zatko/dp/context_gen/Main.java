package sk.zatko.dp.context_gen;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sk.zatko.dp.context_gen.config.AppConfig;

/**
 * Application entry point
 *
 * @author Jozef Zatko
 */
public class Main {

	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		PageFlowContextGen pageFlowContextGen = context.getBean("pageFlowContextGen", PageFlowContextGen.class);
		pageFlowContextGen.associateCodeWithPageFlows();
	}
}
