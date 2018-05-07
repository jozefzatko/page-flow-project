package sk.zatko.dp.logparser.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.zatko.dp.data.accesslog.AccessLog;
import sk.zatko.dp.data.accesslog.LogItem;

/**
 * Parser of access log file
 * Implementation for simple text file where one log item is one line
 *
 * @author Jozef Zatko
 */
@Log4j
@Service
public class LogFileParserServiceSimpleTextImpl implements LogFileParserService {

	@Autowired
	private LogItemParserService logItemParserService;

	@Override
	public AccessLog parseLogFile(String accessLogFilePath) throws IOException {

		log.info("Starting to parse " + accessLogFilePath + " file.");

		AccessLog logs = new AccessLog();
		List<LogItem> logItems = new ArrayList<LogItem>();

		try {

			@Cleanup
			BufferedReader reader = new BufferedReader(new FileReader(accessLogFilePath));

			String line;
			while ((line = reader.readLine()) != null) {

				LogItem item = logItemParserService.parseLogItem(line);

				if (item != null) {
					logItems.add(item);
				}
			}
		} catch (IOException e) {
			log.error("Error while reading " + accessLogFilePath + " file.");
			throw(e);
		}

		log.info("Successfully parsed " + logItems.size() + " log items");

		logs.setLogs(logItems);
		return logs;
	}

}
