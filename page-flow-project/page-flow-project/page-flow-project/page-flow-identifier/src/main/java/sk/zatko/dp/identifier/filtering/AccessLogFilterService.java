package sk.zatko.dp.identifier.filtering;

import sk.zatko.dp.data.accesslog.AccessLog;

/**
 * Filter of parsed access logs
 * Generic interface
 *
 * @author Jozef Zatko
 */
public interface AccessLogFilterService {

	/**
	 *
	 * Filter irrelevant items from access log
	 * @param accessLog Application access log
	 * @return Filtered access log
	 */
	AccessLog filter(AccessLog accessLog);
}
