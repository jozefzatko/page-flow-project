package sk.zatko.dp.pfv_gen;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sk.zatko.dp.pfv_gen.config.AppConfig;

/**
 * @author Jozef Zatko
 */
public class Main {

	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		PageFlowViewsGen pageFlowViewsGen = context.getBean("pageFlowViewsGen", PageFlowViewsGen.class);
		pageFlowViewsGen.generatePageFlowViews();
	}
}
