package sk.zatko.dp.utils;

import java.io.FileReader;
import java.io.IOException;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sk.zatko.dp.data.pageflows.PageFlows;

/**
 * @author Jozef Zatko
 */
@Component("pageFlowUtils")
public class PageFlowUtils {

	private final Gson GSON = Converters.registerDateTime(new GsonBuilder().setPrettyPrinting()).create();

	@Value("${page_flows.file_path}")
	private String pageFlowsFilePath;

	@Value("${page_flow_context.file_path}")
	private String pageFlowContextFilePath;


	public PageFlows getPageFlows() throws IOException, ProcessingException {
		JsonReader reader = new JsonReader(new FileReader(pageFlowsFilePath));
		return GSON.fromJson(reader, PageFlows.class);
	}

	public PageFlows getPageFlowsExtended() throws IOException, ProcessingException {
		JsonReader reader = new JsonReader(new FileReader(pageFlowContextFilePath));
		return GSON.fromJson(reader, PageFlows.class);
	}
}
