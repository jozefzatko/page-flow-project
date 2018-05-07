package sk.zatko.dp.context_gen.controllers;

import sk.zatko.dp.data.controllers.Controllers;
import sk.zatko.dp.data.pageflows.PageFlows;

/**
 * Assign controllers to steps of page flows
 *
 * @author Jozef Zatko
 */
public interface AssignControllersService {

    /**
     * Assign controllers to steps of page flows
     *
     * @param pageFlows Identified page flows
     * @param controllers List of all controllers
     * @return Page flows with assigned controllers
     */
    PageFlows assignControllersToPageFlows(PageFlows pageFlows, Controllers controllers);
}
