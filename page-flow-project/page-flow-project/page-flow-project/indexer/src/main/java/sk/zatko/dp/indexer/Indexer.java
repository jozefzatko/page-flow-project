package sk.zatko.dp.indexer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sk.zatko.dp.data.index.Index;
import sk.zatko.dp.indexer.indexing.IndexService;
import sk.zatko.dp.indexer.serialization.IndexConfigDeserializationStrategy;
import sk.zatko.dp.indexer.serialization.IndexConfigSerializationStrategy;
import sk.zatko.dp.utils.FileUtils;
import sk.zatko.dp.utils.JsonValidator;

/**
 * Indexer of application files
 *
 * @author Jozef Zatko
 */
@Log4j
public class Indexer {

	private static Gson GSON;

	@Autowired
	IndexService indexService;

	@Autowired
	FileUtils fileUtils;

	@Autowired
	JsonValidator jsonValidator;

	@Value("${source_code.root_directory}")
	public String sourceCodeDirectory;

	@Value("${input_config.schema_path}")
	public String inputConfigSchemaPath;

	@Value("${input_config.file_path}")
	public String inputConfigFilePath;

	@Value("${index.file_path}")
	public String indexFilePath;

	static {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.addDeserializationExclusionStrategy(new IndexConfigDeserializationStrategy());
		gsonBuilder.addSerializationExclusionStrategy(new IndexConfigSerializationStrategy());
		GSON = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
	}

	/**
	 * Extend index config file by files from code base and store them to index json file
	 */
	public void createAndStoreIndex() {

		log.info("Starting to create index file from " + this.sourceCodeDirectory + " based on config " + this.inputConfigFilePath);

		try {

			JsonReader reader = new JsonReader(new FileReader(this.inputConfigFilePath));
			Index indexConfig = GSON.fromJson(reader, Index.class);

			File root = new File(this.sourceCodeDirectory);
			Index index = indexService.indexFilesInDirectory(root, indexConfig);

			fileUtils.saveFile(this.indexFilePath, GSON.toJson(index));

			log.info("Index config was successfully created and stored in " + this.indexFilePath);

		} catch (IOException | RuntimeException e) {
			log.error(e);
		}
	}
}
