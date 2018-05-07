package sk.zatko.dp.logparser.parser;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.log4j.Log4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.zatko.dp.data.accesslog.LogItem;

/**
 * Parser of one log item
 * Apache Tomcat Implementation
 *
 * @author Jozef Zatko
 */
@Log4j
@Service
public class LogItemParserServiceApacheTomcatImpl implements LogItemParserService {

	@Value("${access_log.regex_match_pattern}")
	private String accessLogPattern;

	@Value("${access_log.datetime_format}")
	private String dateTimeFormat;

	@Value("${access_log.position.client_ip_address}")
	private int clientIpAddressPosition;

	@Value("${access_log.position.timestamp}")
	private int timestampPosition;

	@Value("${access_log.position.http_method}")
	private int httpMethodPosition;

	@Value("${access_log.position.http_request_path}")
	private int httpRequestPathPosition;

	@Value("${access_log.position.response_code}")
	private int responseCodePosition;

	@Value("${access_log.position.user_session_id}")
	private int userSessionIdPosition;

	@Value("${access_log.position.user_agent}")
	private int userAgentPosition;

	@Value("${access_log.request_path.prefix}")
	private String requestPathPrefix;

	/**
	 * Parse one log file line - convert it into LogItem object using regex matcher
	 */
	public LogItem parseLogItem(String logItemString) {

		Matcher matcher = Pattern.compile(this.accessLogPattern).matcher(logItemString);
		LogItem logItem = null;

		if (matcher.find( )) {

			logItem = new LogItem();

			logItem.setClientIpAddress(matcher.group(this.clientIpAddressPosition).trim());

			try {
				logItem.setTimestamp(getDate(matcher.group(this.timestampPosition).trim()));
			} catch (ParseException e) {
				log.error("Error while parsing timestamp: " + matcher.group(this.timestampPosition));
				log.error(e);
			}

			logItem.setHttpMethod(matcher.group(this.httpMethodPosition).trim());

			String reqPath = requestPathFilter(matcher.group(this.httpRequestPathPosition).trim());
			if (reqPath == null) {
				return null;
			}
			logItem.setHttpRequestPath(reqPath);

			try {
				logItem.setResponseCode(Integer.parseInt(matcher.group(this.responseCodePosition).trim()));
			} catch (NumberFormatException e) {
				log.error("Error while parsing response code: " + matcher.group(this.responseCodePosition).trim());
				log.error(e);
			}

			logItem.setUserSessionId(matcher.group(this.userSessionIdPosition).trim());
			logItem.setUserAgent(matcher.group(this.userAgentPosition).trim());
		}

		return  logItem;
	}

	/**
	 * Timestamp parser
	 */
	private DateTime getDate(String stringDate) throws ParseException {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(this.dateTimeFormat).withLocale(Locale.US);
		return formatter.parseDateTime(stringDate).toDateTime();
	}

	/**
	 * Filter request path prefix from access log
	 */
	private String requestPathFilter(String requestPath) {
		if (requestPath.startsWith(requestPathPrefix)) {
			return requestPath.substring(requestPathPrefix.length());
		}
		return null;
	}
}
