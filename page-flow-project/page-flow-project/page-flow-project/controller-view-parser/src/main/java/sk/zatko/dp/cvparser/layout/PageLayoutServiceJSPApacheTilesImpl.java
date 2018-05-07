package sk.zatko.dp.cvparser.layout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import sk.zatko.dp.data.controllers.Controller;
import sk.zatko.dp.data.controllers.Controllers;

/**
 * Implementation of PageLayoutService for JSP and Apache tiles
 *
 * @author Jozef Zatko
 */
@Log4j
@Service
public class PageLayoutServiceJSPApacheTilesImpl implements PageLayoutService {

	private static final String HYPERTEXT_LINK_REGEX_PATTERN = "<a href=\"<c:url value=\"(\\S+)\" ?\\/>\"";

	private static final String VIEW_REQ_MAPPING_PARAM_REGEX_PATTERN = "\\$\\{([^{}\\$]+)\\}";
	private static final String VIEW_REQ_MAPPING_PARAM_REPLACE = "\\{}";

	private static final String CONTROLLER_REQ_MAPPING_PARAM_REGEX_PATTERN = "\\{([^{}\\$]+)\\}";
	private static final String CONTROLLER_REQ_MAPPING_PARAM_REPLACE = "{}";

	private static final List<String> LAYOUT_VIEWS = new ArrayList<String>();
	static {

		LAYOUT_VIEWS.add("greenhouse//src//main//webapp//WEB-INF//layouts//standard//header.jsp");
		LAYOUT_VIEWS.add("greenhouse//src//main//webapp//WEB-INF//layouts//standard//footer.jsp");
	}

	@Override
	public List<String> getHypertextLinksFromLayout() {

		List<String> hypertextLinks = new ArrayList<String>();
		Pattern linkPattern = Pattern.compile(HYPERTEXT_LINK_REGEX_PATTERN);
		Pattern paramPattern = Pattern.compile(VIEW_REQ_MAPPING_PARAM_REGEX_PATTERN);

		for (String jspFile : LAYOUT_VIEWS) {

			try {

				@Cleanup
				BufferedReader reader = new BufferedReader(new FileReader(jspFile));

				String line;
				while ((line = reader.readLine()) != null) {

					Matcher linkMatcher = linkPattern.matcher(line);
					while (linkMatcher.find()) {

						String hypertextLink = linkMatcher.group(1);

						Matcher paramMatcher = paramPattern.matcher(hypertextLink);
						hypertextLink = paramMatcher.replaceAll(VIEW_REQ_MAPPING_PARAM_REPLACE);
						hypertextLinks.add(hypertextLink);
					}
				}

			} catch (IOException e) {
				log.error(e);
			}
		}
		return hypertextLinks;
	}

	@Override
	public Controllers mapLayoutLinksToControllers(final List<String> layoutLinks, final Controllers controllers) {

		HashMap<String, String> layoutLinksHashMap = new HashMap<>();

		for (String layoutLink : layoutLinks) {
			layoutLinksHashMap.put(layoutLink,layoutLink);
		}

		Pattern paramPattern = Pattern.compile(CONTROLLER_REQ_MAPPING_PARAM_REGEX_PATTERN);

		for (Controller controller : controllers.getControllers()) {

			controller.setAlwaysAccessible(false);

			String controllerReqMapping = controller.getRequestMapping();

			Matcher paramMatcher = paramPattern.matcher(controllerReqMapping);
			controllerReqMapping = paramMatcher.replaceAll(CONTROLLER_REQ_MAPPING_PARAM_REPLACE);

			if (layoutLinksHashMap.containsKey(controllerReqMapping)) {
				controller.setAlwaysAccessible(true);
			}
		}

		return controllers;
	}
}
