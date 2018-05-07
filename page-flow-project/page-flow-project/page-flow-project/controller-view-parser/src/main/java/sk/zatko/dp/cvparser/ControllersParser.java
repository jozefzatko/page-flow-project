package sk.zatko.dp.cvparser;

import java.io.IOException;
import java.util.List;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sk.zatko.dp.cvparser.controllers.ControllerParserService;
import sk.zatko.dp.cvparser.layout.PageLayoutService;
import sk.zatko.dp.data.controllers.Controllers;
import sk.zatko.dp.utils.FileUtils;
import sk.zatko.dp.utils.IndexUtils;

/**
 * Parser for application controllers
 *
 * @author Jozef Zatko
 */
@Log4j
public class ControllersParser {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	@Autowired
	ControllerParserService controllerParserService;

	@Autowired
	PageLayoutService pageLayoutService;

	@Autowired
	IndexUtils indexUtils;

	@Autowired
	FileUtils fileUtils;

	@Value("${controllers.file_path}")
	String controllersFilePath;

	/**
	 * Parse controllers and store its metadata into json file
	 */
	public void parseAndStoreApplicationControllers() {

		log.info("Starting to parse application controllers");

		try {
			Controllers controllers = controllerParserService.parseControllers(indexUtils.getIndex());

			log.info("Processed " + controllers.getControllers().size() + " controllers.");

			List<String> hypertextLinks = pageLayoutService.getHypertextLinksFromLayout();
			controllers = pageLayoutService.mapLayoutLinksToControllers(hypertextLinks, controllers);

			this.fileUtils.saveFile(this.controllersFilePath, GSON.toJson(controllers));

			log.info("Page controllers were successfully stored in " + this.controllersFilePath + ".");

		} catch (IOException | ProcessingException e) {
			log.error(e);
		}
	}

}
