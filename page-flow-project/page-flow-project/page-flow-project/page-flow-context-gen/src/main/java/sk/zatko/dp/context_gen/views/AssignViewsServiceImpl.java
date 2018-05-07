package sk.zatko.dp.context_gen.views;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import sk.zatko.dp.data.controllers.Controllers;
import sk.zatko.dp.data.pageflows.PageFlow;
import sk.zatko.dp.data.pageflows.PageFlowStep;
import sk.zatko.dp.data.pageflows.PageFlows;
import sk.zatko.dp.data.views.View;
import sk.zatko.dp.data.views.Views;

/**
 * Assign controllers to steps of page flows
 * Implementation
 *
 * @author Jozef Zatko
 */
@Service
public class AssignViewsServiceImpl implements AssignViewsService {

	private static final String RETURN_VIEW_REGEX = "return \"(\\S+)\";";
	private static final String REDIRECT_REGEX = "redirect:.*";

	@Override
	public PageFlows assignViewsAccordingToController(final PageFlows pageFlows, final Controllers controllers, final Views views) {

		HashMap<String, View> viewHashMap = new LinkedHashMap<>();
		for (View view : views.getViews()) {
			viewHashMap.put(view.getLogicalName(), view);
		}

		final Pattern returnViewPattern = Pattern.compile(RETURN_VIEW_REGEX);

		for (PageFlow pageFlow : pageFlows.getPageFlows()) {
			for (PageFlowStep pageFlowStep : pageFlow.getPageFlowSteps()) {

				if (pageFlowStep.getViews() == null) {
					pageFlowStep.setViews(new HashSet<View>());
				}

				Matcher returnViewMatcher = returnViewPattern.matcher(pageFlowStep.getPageController().getMethodBody());

				while (returnViewMatcher.find()) {

					if (returnViewMatcher.group(1).matches(REDIRECT_REGEX)) {
						continue;
					}
					if (viewHashMap.get(returnViewMatcher.group(1)) != null) {
						pageFlowStep.getViews().add(viewHashMap.get(returnViewMatcher.group(1)));
					}
				}

				if (pageFlowStep.getViews().isEmpty() && "GET".equals(pageFlowStep.getHttpMethod())) {

					String logicalName = pageFlowStep.getPageController().getRequestMapping();
					if (logicalName.startsWith("/")) {
						logicalName = logicalName.substring(1);
					}
					if (viewHashMap.get(logicalName) != null) {
						pageFlowStep.getViews().add(viewHashMap.get(logicalName));
					}
				}
			}
		}

		return pageFlows;
	}

	@Override
	public PageFlows assignViewsAccordingToPageFlows(final PageFlows pageFlows, final Controllers controllers, final Views views) {

		for (PageFlow pageFlow : pageFlows.getPageFlows()) {
			for (int i=0; i<pageFlow.getPageFlowSteps().size()-1; i++) {

				PageFlowStep currentStep = pageFlow.getPageFlowSteps().get(i);
				PageFlowStep nextStep = pageFlow.getPageFlowSteps().get(i+1);

				if (currentStep.getViews() != null && !currentStep.getViews().isEmpty()) {
					continue;
				}

				if (currentStep.getRelatedViews() == null) {
					currentStep.setRelatedViews(new HashSet<View>());
				}

				String controllerPathRegex = fromSpringPathToRegex(nextStep.getPageController().getRequestMapping());

				for (View view : views.getViews()) {
					for (String link : view.getLinks()) {

						if (link.matches(controllerPathRegex)) {

							currentStep.getRelatedViews().add(view);
						}
					}
				}

				for (View view : currentStep.getViews()) {
					for (Iterator<View> viewIterator = currentStep.getRelatedViews().iterator(); viewIterator.hasNext() ; ) {
						if (view.equals(viewIterator.next())) {
							viewIterator.remove();
						}
					}
				}
			}

		}

		return pageFlows;
	}

	/**
	 * Convert Spring MVC request path expression into regex
	 *
	 * @param reqPath Spring MVC controller request path
	 * @return Regex from request controller path
	 */
	private String fromSpringPathToRegex(String reqPath) {

		return reqPath.replace("@self", "{username}").replaceAll("\\{[^\\{\\}\\/]*\\}", "[^\\/]*");
	}
}
