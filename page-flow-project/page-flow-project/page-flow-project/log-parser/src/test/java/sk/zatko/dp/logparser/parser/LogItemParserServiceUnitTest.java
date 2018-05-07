package sk.zatko.dp.logparser.parser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sk.zatko.dp.data.accesslog.LogItem;
import sk.zatko.dp.logparser.config.TestConfig;

/**
 * Unit test for @see sk.zatko.dp.logparser.parser.LogItemParserService
 *
 * @author Jozef Zatko
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource("classpath:test.properties")
public class LogItemParserServiceUnitTest {

	private static final String CORRECT_LOG_1 = "147.175.177.17 - - [04/Dec/2017:22:41:40 +0100] \" GET /greenhouse/ HTTP/1.1 \" 200 CBFEAF64BFCFCD3F98226C9843C65213 Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36";
	private static final String CORRECT_LOG_2 = "147.175.177.17 - - [04/Dec/2017:22:41:31 +0100] \" POST /greenhouse/signup HTTP/1.1 \" 200 - Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36";

	private static final String WRONG_LOG_1 = "this is not a correct log item";
	private static final String WRONG_LOG_2 = "147.175.177.17 - [04/Dec/2017:22:41:31 +0100] \" POST /greenhouse/signup HTTP/1.1 \" 200 - Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36";

	@Autowired
	LogItemParserService logItemParserService;


	@Test
	public void testCorrectLog1() {

		LogItem logItem = logItemParserService.parseLogItem(CORRECT_LOG_1);

		Assert.assertNotNull(logItem);
		Assert.assertNotNull(logItem.getHttpMethod());
		Assert.assertNotNull(logItem.getHttpRequestPath());
		Assert.assertNotNull(logItem.getTimestamp());
		Assert.assertNotNull(logItem.getUserAgent());
		Assert.assertNotNull(logItem.getUserId());
		Assert.assertNotNull(logItem.getUserSessionId());
	}

	@Test
	public void testCorrectLog2() {

		LogItem logItem = logItemParserService.parseLogItem(CORRECT_LOG_2);

		Assert.assertNotNull(logItem);
		Assert.assertNotNull(logItem.getHttpMethod());
		Assert.assertNotNull(logItem.getHttpRequestPath());
		Assert.assertNotNull(logItem.getTimestamp());
		Assert.assertNotNull(logItem.getUserAgent());
		Assert.assertNotNull(logItem.getUserId());
		Assert.assertNotNull(logItem.getUserSessionId());
	}

	@Test
	public void testWrongLog1() {
		LogItem logItem = logItemParserService.parseLogItem(WRONG_LOG_1);
		Assert.assertNull(logItem);
	}

	@Test
	public void testWrongLog2() {
		LogItem logItem = logItemParserService.parseLogItem(WRONG_LOG_2);
		Assert.assertNull(logItem);
	}

}
