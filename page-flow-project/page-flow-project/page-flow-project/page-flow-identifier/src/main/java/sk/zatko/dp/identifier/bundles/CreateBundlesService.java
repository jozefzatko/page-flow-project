package sk.zatko.dp.identifier.bundles;

import sk.zatko.dp.data.accesslog.AccessLog;
import sk.zatko.dp.data.bundles.Bundles;

/**
 * Service creating bundles - access log items aggregated according to users
 *
 * @author Jozef Zatko
 */
public interface CreateBundlesService {

	/**
	 * Create bundles object from access log by aggregating logs according to users
	 *
	 * @param accessLog parsed access logs
	 * @return Bundle object
	 */
	Bundles createBundledLog(final AccessLog accessLog);
}
