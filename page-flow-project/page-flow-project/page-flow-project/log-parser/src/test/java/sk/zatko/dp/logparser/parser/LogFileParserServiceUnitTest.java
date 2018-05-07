package sk.zatko.dp.logparser.parser;

import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;
import sk.zatko.dp.data.accesslog.AccessLog;
import sk.zatko.dp.data.accesslog.LogItem;

/**
 * Unit test for @see sk.zatko.dp.logparser.parser.LogFileParserService
 *
 * @author Jozef Zatko
 */
@RunWith(MockitoJUnitRunner.class)
@TestPropertySource("test.properties")
public class LogFileParserServiceUnitTest {

	private static final String RIGHT_ACCESS_LOG_FILE_PATH = "src//test//resources//test_access_log.txt";
	private static final String WRONG_ACCESS_LOG_FILE_PATH = "src//test//resources//some_access_log.txt";

	@InjectMocks
	private LogFileParserServiceSimpleTextImpl logFileParserService;

	@Mock
	LogItemParserService logItemParserService;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		when(logItemParserService.parseLogItem(Mockito.any(String.class))).thenReturn(new LogItem());

	}

	@Test
	public void testCorrectFile() throws IOException {
		AccessLog accessLog = logFileParserService.parseLogFile(RIGHT_ACCESS_LOG_FILE_PATH);

		Assert.assertNotNull(accessLog);
		Assert.assertEquals(accessLog.getLogs().size(), 9);
	}

	@Test(expected = IOException.class)
	public void testWrongFile() throws IOException {
		logFileParserService.parseLogFile(WRONG_ACCESS_LOG_FILE_PATH);
		Assert.fail();
	}
}
