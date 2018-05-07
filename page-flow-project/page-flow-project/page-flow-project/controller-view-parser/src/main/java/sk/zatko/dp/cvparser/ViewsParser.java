package sk.zatko.dp.cvparser;

import java.io.IOException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sk.zatko.dp.cvparser.views.ViewParserService;
import sk.zatko.dp.data.views.Views;
import sk.zatko.dp.utils.FileUtils;
import sk.zatko.dp.utils.IndexUtils;

/**
 * Parser for application views
 *
 * @author Jozef Zatko
 */
@Log4j
public class ViewsParser {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	@Autowired
	ViewParserService viewParserService;

	@Autowired
	IndexUtils indexUtils;

	@Autowired
	FileUtils fileUtils;

	@Value("${views.file_path}")
	private String viewsFilePath;

	/**
	 * Parse views and store its metadata into json file
	 */
	public void parseAndStoreApplicationViews() {

		log.info("Starting to parse application views");

		try {
			Views views = viewParserService.parseViews(indexUtils.getIndex());

			log.info("Processed " + views.getViews().size() + " views.");

			views = viewParserService.findHypertextLinks(views);

			this.fileUtils.saveFile(this.viewsFilePath, GSON.toJson(views));

			log.info("Page views were successfully stored in " + this.viewsFilePath + ".");

		} catch (IOException | ProcessingException e) {
			log.error(e);
		}
	}
}
