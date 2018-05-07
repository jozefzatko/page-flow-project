package sk.zatko.dp.logparser.parser;

import java.io.IOException;

import sk.zatko.dp.data.accesslog.AccessLog;

/**
 * Parser of access log file
 * Generic interface
 *
 * @author Jozef Zatko
 */
public interface LogFileParserService {

    /**
     * Parse log file and store data in the form of AccessLog object
     *
     * @param accessLogFilePath File path to parsed log file
     * @return AccessLog object
     * @throws IOException File not found or cannot be properly open
     */
    AccessLog parseLogFile(String accessLogFilePath) throws IOException;

}
