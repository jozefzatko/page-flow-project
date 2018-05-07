package sk.zatko.dp.pfv_gen;

import java.io.IOException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sk.zatko.dp.data.pageflows.PageFlow;
import sk.zatko.dp.data.pageflows.PageFlows;
import sk.zatko.dp.pfv_gen.generator.SinglePageFlowViewGenService;
import sk.zatko.dp.utils.FileUtils;
import sk.zatko.dp.utils.PageFlowUtils;

/**
 * @author Jozef Zatko
 */
public class PageFlowViewsGen {

	@Value("${page_flow_views.folder_path}")
	private String pageFlowViewsFolderPath;

	@Autowired
	private PageFlowUtils pageFlowUtils;

	@Autowired
	private FileUtils fileUtils;

	@Autowired
	private SinglePageFlowViewGenService singlePageFlowViewGenService;

	public void generatePageFlowViews() {

		try {
			PageFlows pageFlows = pageFlowUtils.getPageFlowsExtended();

			int i=1;
			for (PageFlow pageFlow : pageFlows.getPageFlows()) {

				String pageFlowView = singlePageFlowViewGenService.generateSinglePageFlowView(pageFlow);
				fileUtils.createDirIfNotExist(pageFlowViewsFolderPath);
				fileUtils.saveFile(pageFlowViewsFolderPath + "//" + i++ + ".md", pageFlowView);
			}

		} catch (IOException | ProcessingException e) {
			e.printStackTrace();
		}
	}
}
