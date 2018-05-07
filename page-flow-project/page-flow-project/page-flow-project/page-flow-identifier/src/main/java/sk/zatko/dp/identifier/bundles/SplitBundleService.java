package sk.zatko.dp.identifier.bundles;

import sk.zatko.dp.data.bundles.Bundles;

/**
 * Split bundle into multiple ones
 * Generic interface
 *
 * @author Jozef Zatko
 */
public interface SplitBundleService {

	/**
	 * Split each bundle into multiple ones
	 *
	 * @param bundledLog User session pages created from access logs
	 * @return Bundles after split
	 */
	Bundles splitBundles(final Bundles bundledLog);
}
