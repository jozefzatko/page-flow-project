package sk.zatko.dp.cvparser.layout;

import java.util.List;

import sk.zatko.dp.data.controllers.Controllers;

/**
 * Generic interface. Need to be implemented based on used frontend technology.
 *
 * @author Jozef Zatko
 */
public interface PageLayoutService {

	/**
	 * Find all hyperlinks from pages of application layout (header, footer, side panels,....)
	 *
	 * @return All found hyperlinks from application layout
	 */
	List<String> getHypertextLinksFromLayout();


	/**
	 * Set alwaysAccessible attribute to true for controllers handling requests from layout links
	 * Set alwaysAccessible attribute to false for other controllers.
	 *
	 * @param layoutLinks All found hyperlinks from application layout
	 * @param controllers All application controllers without alwaysAccessible attribute
	 * @return Application controllers with set alwaysAccessible attribute
	 */
	Controllers mapLayoutLinksToControllers(final List<String> layoutLinks, final Controllers controllers);
}
