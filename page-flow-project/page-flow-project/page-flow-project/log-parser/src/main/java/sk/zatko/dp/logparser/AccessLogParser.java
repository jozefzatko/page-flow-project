package sk.zatko.dp.logparser;

import java.io.IOException;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sk.zatko.dp.data.accesslog.AccessLog;
import sk.zatko.dp.logparser.parser.LogFileParserService;
import sk.zatko.dp.utils.FileUtils;

/**
 * Access Log Parser
 *
 * @author Jozef Zatko
 */
@Log4j
public class AccessLogParser {

    private final Gson GSON = Converters.registerDateTime(new GsonBuilder().setPrettyPrinting()).create();

    @Autowired
    private LogFileParserService logFileParserService;

    @Autowired
    private FileUtils fileUtils;

    @Value("${access_log.raw.file_path}")
    private String rawAccessLogFilePath;

    @Value("${access_log.parsed.file_path}")
    private String parsedAccessLogFilePath;

    /**
     * Transform raw access logs into AccessLog format using LogFileParserService
     */
    public void parseRawAccessLog() {

        try {
            AccessLog accessLog = logFileParserService.parseLogFile(this.rawAccessLogFilePath);
            String accessLogJson = GSON.toJson(accessLog);
            fileUtils.saveFile(this.parsedAccessLogFilePath, accessLogJson);

            log.info("Parsed access log items was successfully saved in " + this.parsedAccessLogFilePath);

        } catch (IOException e) {
            log.error(e);
        }
    }

}
