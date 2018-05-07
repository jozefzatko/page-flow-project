package sk.zatko.dp.identifier;

import java.io.FileReader;
import java.io.IOException;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sk.zatko.dp.data.accesslog.AccessLog;
import sk.zatko.dp.data.bundles.Bundles;
import sk.zatko.dp.data.controllers.Controllers;
import sk.zatko.dp.data.pageflows.PageFlow;
import sk.zatko.dp.data.pageflows.PageFlows;
import sk.zatko.dp.identifier.bundles.RequestMappingService;
import sk.zatko.dp.identifier.bundles.SplitBundleService;
import sk.zatko.dp.identifier.bundles.CreateBundlesService;
import sk.zatko.dp.identifier.filtering.AccessLogFilterService;
import sk.zatko.dp.identifier.postprocessing.PageFlowsPostprocessingService;
import sk.zatko.dp.identifier.seq_mining.PageFlowsMiningService;
import sk.zatko.dp.utils.FileUtils;

/**
 * Identify page flows based on parsed access logs and parsed views and controllers
 *
 * @author Jozef Zatko
 */
@Log4j
public class PageFlowIdentifier {

	private final Gson GSON = Converters.registerDateTime(new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

	@Autowired
	private AccessLogFilterService accessLogFilterService;

	@Autowired
	private CreateBundlesService createBundlesService;

	@Autowired
	private RequestMappingService requestMappingService;

	@Autowired
	private SplitBundleService splitBundleService;

	@Autowired
	private PageFlowsMiningService pageFlowsMiningService;

	@Autowired
	private PageFlowsPostprocessingService pageFlowsPostprocessingService;

	@Autowired
	private FileUtils fileUtils;

	@Value("${access_log.parsed.file_path}")
	private String accessLogFilePath;

	@Value("${controllers.file_path}")
	private String controllersFilePath;

	@Value("${bundles.file_path}")
	private String bundlesFilePath;

	@Value("${page_flows.file_path}")
	private String pageFlowsFilePath;

	@Value("${page_flows_seq.file_path}")
	private String pageFlowsSeqFilePath;

	public void identifyAndStorePageFlows() {

		log.info("Starting to identify page flows from access log: " + accessLogFilePath + ".");

		try {
			AccessLog accessLog = getAccessLog();
			AccessLog filteredAccessLog = accessLogFilterService.filter(accessLog);

			Bundles bundles = createBundlesService.createBundledLog(filteredAccessLog);
			Bundles mappedSplitBundles = requestMappingService.mapRequestMapping(bundles, getControllers());
			Bundles splitBundles = splitBundleService.splitBundles(mappedSplitBundles);

			fileUtils.saveFile(bundlesFilePath, GSON.toJson(splitBundles));

			PageFlows rawPageFlows = pageFlowsMiningService.getRawPageFlows(splitBundles);
			PageFlows pageFlows = pageFlowsPostprocessingService.postProcess(rawPageFlows, splitBundles, getControllers());

			fileUtils.saveFile(pageFlowsFilePath, GSON.toJson(pageFlows));

			StringBuilder sb = new StringBuilder("");
			for (PageFlow pageFlow : pageFlows.getPageFlows()) {
				sb.append(pageFlow.getSequence());
				sb.append("\n");
			}
			fileUtils.saveFile(pageFlowsSeqFilePath, sb.toString());

		} catch (IOException | ProcessingException e) {
			log.error(e);
		}
	}

	/**
	 * Load access logs from JSON file
	 */
	private AccessLog getAccessLog() throws IOException, ProcessingException {

		JsonReader reader = new JsonReader(new FileReader(this.accessLogFilePath));
		return GSON.fromJson(reader, AccessLog.class);
	}

	/**
	 * Load application controllers from JSON file
	 */
	private Controllers getControllers() throws IOException, ProcessingException {

		JsonReader reader = new JsonReader(new FileReader(this.controllersFilePath));
		return GSON.fromJson(reader, Controllers.class);
	}

}
