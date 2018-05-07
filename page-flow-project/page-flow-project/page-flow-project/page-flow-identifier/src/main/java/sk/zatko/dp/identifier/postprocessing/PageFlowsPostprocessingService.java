package sk.zatko.dp.identifier.postprocessing;

import sk.zatko.dp.data.bundles.Bundles;
import sk.zatko.dp.data.controllers.Controllers;
import sk.zatko.dp.data.pageflows.PageFlows;
import sk.zatko.dp.data.views.Views;

/**
 * Page Flows postprocessing
 *
 * @author Jozef Zatko
 */
public interface PageFlowsPostprocessingService {

	/**
	 *
	 *
	 * @param pageFlows Page flows object - identified page flows
	 * @param bundles Filtered application requests aggregated according to user session
	 * @param controllers All application views
	 * @return Processed page flows
	 */
	PageFlows postProcess(final PageFlows pageFlows, final Bundles bundles, final Controllers controllers);
}
