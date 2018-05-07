package sk.zatko.dp.identifier.bundles;

import sk.zatko.dp.data.bundles.Bundles;
import sk.zatko.dp.data.controllers.Controllers;

/**
 * Map request mapping specified in controllers into bundles
 * Generic implementation
 *
 * @author Jozef Zatko
 */
public interface RequestMappingService {

	/**
	 * Map request mapping specified in controllers into bundles
	 *
	 * @param bundles User session requests created from access logs
	 * @param controllers List of application controllers with metadata
	 * @return Bundles with mapped request mappings from controllers
	 */
	Bundles mapRequestMapping(Bundles bundles, Controllers controllers);
}
