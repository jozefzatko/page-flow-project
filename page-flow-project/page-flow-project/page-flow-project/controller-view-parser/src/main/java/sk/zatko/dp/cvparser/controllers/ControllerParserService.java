package sk.zatko.dp.cvparser.controllers;

import sk.zatko.dp.data.controllers.Controllers;
import sk.zatko.dp.data.index.Index;

/**
 * Generic parser service for controllers
 *
 * @author Jozef Zatko
 */
public interface ControllerParserService {

	/**
	 * Find all page controllers according to index file
	 *
	 * @param index Index file
	 * @return List of all page controllers
	 */
	Controllers parseControllers(final Index index);
}
