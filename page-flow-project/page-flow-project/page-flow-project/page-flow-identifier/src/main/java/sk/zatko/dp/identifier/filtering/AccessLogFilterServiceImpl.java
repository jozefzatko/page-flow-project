package sk.zatko.dp.identifier.filtering;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.zatko.dp.data.accesslog.AccessLog;
import sk.zatko.dp.data.accesslog.LogItem;
import sk.zatko.dp.identifier.filtering.model.AbstractLogFilter;
import sk.zatko.dp.identifier.filtering.model.AccessLogFilter;

/**
 * Filter of parsed access logs
 * Implementation based on JSON filter
 *
 * @author Jozef Zatko
 */
@Log4j
@Service
public class AccessLogFilterServiceImpl implements AccessLogFilterService {

	private static final Gson GSON = new Gson();

	@Value("${log_filters.file_path}")
	private String logFiltersFilePath;

	public AccessLog filter(AccessLog accessLog) {

		try {

			JsonReader reader = new JsonReader(new FileReader(this.logFiltersFilePath));
			AccessLogFilter accessLogFilter = GSON.fromJson(reader, AccessLogFilter.class);

			List<AbstractLogFilter> allFilters = new ArrayList<AbstractLogFilter>();
			allFilters.addAll(accessLogFilter.getHttpMethodFilters());
			allFilters.addAll(accessLogFilter.getHttpRequestPathRegexFilters());
			allFilters.addAll(accessLogFilter.getHttpRequestPathRegexFilters());

			for (Iterator<LogItem> iterator = accessLog.getLogs().listIterator(); iterator.hasNext(); ) {
				LogItem logItem = iterator.next();

				for (AbstractLogFilter filter : allFilters) {
					if (!filter.isIncluded(logItem)) {
						iterator.remove();
						break;
					}
				}
			}

		} catch (IOException e) {
			log.error(e);
		}
		return  accessLog;
	}
}
