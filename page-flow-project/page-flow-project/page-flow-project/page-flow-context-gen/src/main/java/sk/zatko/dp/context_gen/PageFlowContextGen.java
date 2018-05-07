package sk.zatko.dp.context_gen;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sk.zatko.dp.data.controllers.Controllers;
import sk.zatko.dp.data.pageflows.PageFlows;
import sk.zatko.dp.data.views.Views;
import sk.zatko.dp.context_gen.controllers.AssignControllersService;
import sk.zatko.dp.context_gen.views.AssignViewsService;
import sk.zatko.dp.utils.FileUtils;
import sk.zatko.dp.utils.PageFlowUtils;

/**
 * Generate page flow context (page flows associated with code)
 *
 * @author Jozef Zatko
 */
@Log4j
public class PageFlowContextGen {

	private final Gson GSON = Converters.registerDateTime(new GsonBuilder().setPrettyPrinting()).create();

	@Autowired
	private AssignControllersService assignControllersService;

	@Autowired
	private AssignViewsService assignViewsService;

	@Autowired
	private PageFlowUtils pageFlowUtils;

	@Autowired
	private FileUtils fileUtils;

	@Value("${controllers.file_path}")
	private String controllersFilePath;

	@Value("${views.file_path}")
	private String viewsFilePath;

	@Value("${bundles.file_path}")
	private String bundlesFilePath;

	@Value("${page_flow_context.file_path}")
	private String pageFlowContextFilePath;

	/**
	 * Assign controllers and views to page flows
	 */
	public void associateCodeWithPageFlows() {

		try {

			Controllers controllers = getControllers();
			Views views = getViews();
			PageFlows pageFlows = pageFlowUtils.getPageFlows();

			pageFlows = assignControllersService.assignControllersToPageFlows(pageFlows, controllers);

			pageFlows = assignViewsService.assignViewsAccordingToController(pageFlows, controllers, views);
			pageFlows = assignViewsService.assignViewsAccordingToPageFlows(pageFlows, controllers, views);

			fileUtils.saveFile(pageFlowContextFilePath, GSON.toJson(pageFlows));

		} catch (IOException | ProcessingException e) {
			log.error(e);
		}

	}

	/**
	 * Deserialize controllers from JSON into object
	 *
	 * @return Controllers object
	 * @throws IOException Wrong file path
	 * @throws ProcessingException Deserialization problem
	 */
	private Controllers getControllers() throws IOException, ProcessingException {
		JsonReader reader = new JsonReader(new FileReader(controllersFilePath));
		return GSON.fromJson(reader, Controllers.class);
	}

	/**
	 * Deserialize views from JSON into object
	 *
	 * @return Views object
	 * @throws IOException Wrong file path
	 * @throws ProcessingException Deserialization problem
	 */
	private Views getViews() throws IOException, ProcessingException {
		JsonReader reader = new JsonReader(new FileReader(viewsFilePath));
		return GSON.fromJson(reader, Views.class);
	}
}
