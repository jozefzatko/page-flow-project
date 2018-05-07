package sk.zatko.dp.page_flow_vis.controllers.pages;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sk.zatko.dp.data.pageflows.PageFlow;
import sk.zatko.dp.data.pageflows.PageFlows;
import sk.zatko.dp.utils.PageFlowUtils;

/**
 * Controller of implemented pages
 *
 * @author Jozef Zatko
 */
@Controller
public class PageController {

	@Autowired
	PageFlowUtils pageFlowUtils;

	@GetMapping("/")
	public String index(Model model) {

		Map<Integer, String> pageFlowsMap = new HashMap<Integer, String>();

		try {
			PageFlows pageFlows = pageFlowUtils.getPageFlowsExtended();

			for (PageFlow pageFlow : pageFlows.getPageFlows()) {
				pageFlowsMap.put(pageFlow.getId(), pageFlow.getSequence());
			}

		} catch (IOException | ProcessingException e) {
			e.printStackTrace();
		}
		model.addAttribute("pageFlows", pageFlowsMap);

		return "index";
	}

}
