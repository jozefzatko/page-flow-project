package sk.zatko.dp.utils;

import java.io.FileReader;
import java.io.IOException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sk.zatko.dp.data.index.FileType;
import sk.zatko.dp.data.index.Index;

@Component("indexUtils")
public class IndexUtils {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	@Value("${index.file_path}")
	private String indexFilePath;

	public Index getIndex() throws IOException, ProcessingException {
		JsonReader reader = new JsonReader(new FileReader(this.indexFilePath));
		return GSON.fromJson(reader, Index.class);
	}

	public FileType getFileType(String name) throws IOException, ProcessingException {

		for (FileType fileType : getIndex().getFileTypes()) {

			if (fileType.getFileTypeName().equalsIgnoreCase(name)) {
				return fileType;
			}
		}
		return null;
	}
}
