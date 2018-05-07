package sk.zatko.dp.logparser.parser;

import sk.zatko.dp.data.accesslog.LogItem;

/**
 * Parser of one log item
 * Generic interface
 *
 * @author Jozef Zatko
 */
public interface LogItemParserService {

	/**
	 * Parse one log item - convert it into LogItem
	 *
	 * @param logItemString One log item in String format
	 * @return LogItem object
	 */
	LogItem parseLogItem(String logItemString);
}
