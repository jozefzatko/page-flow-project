package sk.zatko.dp.data.pageflows;

import lombok.Data;
import sk.zatko.dp.data.controllers.Controller;
import sk.zatko.dp.data.views.View;

import java.util.Set;

@Data
public class PageFlowStep {

    private String requestPath;
    private String httpMethod;

    private Controller pageController;
    private Set<Controller> restControllers;

    private Set<View> views;
    private Set<View> relatedViews;

    private Set<String> ajaxRequests;
}
