package sk.zatko.dp.context_gen.views;

import sk.zatko.dp.data.controllers.Controllers;
import sk.zatko.dp.data.pageflows.PageFlows;
import sk.zatko.dp.data.views.Views;

/**
 * Assign views to steps of page flows
 *
 * @author Jozef Zatko
 */
public interface AssignViewsService {

	/**
	 * Assign views to steps of page flows according to already assigned controller
	 *
	 * @param pageFlows
	 * @param controllers
	 * @param views
	 * @return
	 */
	PageFlows assignViewsAccordingToController(final PageFlows pageFlows, final Controllers controllers, final Views views);

	/**
	 * Assign views to steps of page flows according to page flow
	 *
	 * @param pageFlows
	 * @param controllers
	 * @param views
	 * @return
	 */
	PageFlows assignViewsAccordingToPageFlows(final PageFlows pageFlows, final Controllers controllers, final Views views);
}
