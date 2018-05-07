package sk.zatko.dp.indexer.indexing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sk.zatko.dp.data.index.Index;
import sk.zatko.dp.indexer.config.TestConfig;
import sk.zatko.dp.indexer.serialization.IndexConfigDeserializationStrategy;
import sk.zatko.dp.indexer.serialization.IndexConfigSerializationStrategy;

/**
 * Test for IndexService
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource("classpath:test.properties")
public class IndexDirectoryTest {

	@Value("${source_code.root_directory}")
	public String sourceCodeDirectory;

	@Value("${input_config.file_path}")
	public String inputConfigFilePath;

	@Autowired
	IndexService indexService;

	Index indexConfig;

	@Before
	public void initIndex() throws FileNotFoundException {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.addDeserializationExclusionStrategy(new IndexConfigDeserializationStrategy());
		gsonBuilder.addSerializationExclusionStrategy(new IndexConfigSerializationStrategy());
		Gson GSON = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();

		JsonReader reader = new JsonReader(new FileReader(this.inputConfigFilePath));
		indexConfig = GSON.fromJson(reader, Index.class);
	}

	@Test
	public void testIndexer() {

		Index result = indexService.indexFilesInDirectory(new File(sourceCodeDirectory), indexConfig);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.getFileTypes().get(0).getFileItems().size());
		Assert.assertEquals(1, result.getFileTypes().get(1).getFileItems().size());
	}

}
