package sk.zatko.dp.context_gen.controllers;

import org.springframework.stereotype.Service;
import sk.zatko.dp.data.controllers.Controller;
import sk.zatko.dp.data.controllers.Controllers;
import sk.zatko.dp.data.pageflows.PageFlow;
import sk.zatko.dp.data.pageflows.PageFlowStep;
import sk.zatko.dp.data.pageflows.PageFlows;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Assign controllers to steps of page flows
 * Implementation
 *
 * @author Jozef Zatko
 */
@Service
public class AssignControllersServiceImpl implements AssignControllersService {

    @Override
    public PageFlows assignControllersToPageFlows(PageFlows pageFlows, Controllers controllers) {

        HashMap<String, Controller> controllerHashMap = new LinkedHashMap<>();
        for (Controller controller : controllers.getControllers()) {
            controllerHashMap.put(controller.getRequestMapping() + "&" + controller.getHttpMethod(), controller);
        }

        for (PageFlow pageFlow : pageFlows.getPageFlows()) {
            for (PageFlowStep pageFlowStep : pageFlow.getPageFlowSteps()) {

                pageFlowStep.setPageController(controllerHashMap.get(pageFlowStep.getRequestPath() + "&" + pageFlowStep.getHttpMethod()));

                if (pageFlowStep.getAjaxRequests() != null) {

                    pageFlowStep.setRestControllers(new HashSet<Controller>());
                    for (String ajaxRequest : pageFlowStep.getAjaxRequests()) {
                        pageFlowStep.getRestControllers().add(controllerHashMap.get(ajaxRequest));
                    }
                }
            }
        }

        return pageFlows;
    }
}
