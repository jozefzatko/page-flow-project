package sk.zatko.dp.logparser.parser;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sk.zatko.dp.data.accesslog.AccessLog;
import sk.zatko.dp.logparser.config.TestConfig;

/**
 * Assembly test for @see sk.zatko.dp.logparser.parser.LogFileParserService
 *
 * @author Jozef Zatko
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource("classpath:test.properties")
public class LogFileParserServiceAssemblyTest {

	private static final String RIGHT_ACCESS_LOG_FILE_PATH = "src//test//resources//test_access_log.txt";
	private static final String WRONG_ACCESS_LOG_FILE_PATH = "src//test//resources//non_existing_file.txt";

	@Autowired
	private LogFileParserService logFileParserService;

	@Test
	public void testCorrect() throws IOException {

		AccessLog accessLog = logFileParserService.parseLogFile(RIGHT_ACCESS_LOG_FILE_PATH);

		Assert.assertNotNull(accessLog);
		Assert.assertEquals(accessLog.getLogs().size(), 7);
	}

	@Test(expected = FileNotFoundException.class)
	public void testToFail() throws IOException {

		logFileParserService.parseLogFile(WRONG_ACCESS_LOG_FILE_PATH);
		Assert.fail();
	}
}
