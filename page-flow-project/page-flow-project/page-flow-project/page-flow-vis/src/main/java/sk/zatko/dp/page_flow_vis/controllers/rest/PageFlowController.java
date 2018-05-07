package sk.zatko.dp.page_flow_vis.controllers.rest;

import java.io.IOException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sk.zatko.dp.data.pageflows.PageFlow;
import sk.zatko.dp.data.pageflows.PageFlows;
import sk.zatko.dp.page_flow_vis.converters.FromPageFlowToPageFlowContext;
import sk.zatko.dp.page_flow_vis.converters.FromPageFlowsToPageFlowGraph;
import sk.zatko.dp.page_flow_vis.models.context.PageFlowContext;
import sk.zatko.dp.page_flow_vis.models.graph.PageFlowGraph;
import sk.zatko.dp.utils.PageFlowUtils;

/**
 * REST API providing page flow for vis.js - visualization library
 *
 * @author Jozef Zatko
 */
@RestController
@RequestMapping("/api")
public class PageFlowController {

    @Autowired
    PageFlowUtils pageFlowUtils;

    @Autowired
    FromPageFlowsToPageFlowGraph graphConverter;

    @Autowired
    FromPageFlowToPageFlowContext contextConverter;

    @RequestMapping(value = "/getPageFlowGraph", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public PageFlowGraph getPageFlowGraph() throws IOException, ProcessingException {
        return graphConverter.convert(pageFlowUtils.getPageFlows());
    }

    @RequestMapping(value = "/getPageFlowContext/{pageFlowId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public PageFlowContext getPageFlowContext(@PathVariable("pageFlowId") final int pageFlowId) throws IOException, ProcessingException {

        PageFlows pageFlows = pageFlowUtils.getPageFlowsExtended();

        for (PageFlow pageFlow : pageFlows.getPageFlows()) {
            if (pageFlow.getId() == pageFlowId) {
                return contextConverter.convert(pageFlow);
            }
        }
        return null;
    }
}
