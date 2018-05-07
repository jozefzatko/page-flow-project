package sk.zatko.dp.page_flow_vis.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sk.zatko.dp.data.controllers.Controller;
import sk.zatko.dp.data.pageflows.PageFlow;
import sk.zatko.dp.data.pageflows.PageFlowStep;
import sk.zatko.dp.data.views.View;
import sk.zatko.dp.page_flow_vis.models.EdgeColor;
import sk.zatko.dp.page_flow_vis.models.NodeColor;
import sk.zatko.dp.page_flow_vis.models.context.ContextEdge;
import sk.zatko.dp.page_flow_vis.models.context.ContextNode;
import sk.zatko.dp.page_flow_vis.models.context.ContextNodeType;
import sk.zatko.dp.page_flow_vis.models.Font;
import sk.zatko.dp.page_flow_vis.models.context.PageFlowContext;

/**
 * Convert PageFlow object to PageFlowContext object
 *
 * @author Jozef Zatko
 */
@Component
public class FromPageFlowToPageFlowContext implements Converter<PageFlow, PageFlowContext> {

	private static final String PAGE_STEREOTYPE = "page";
	private static final String PAGE_CONTROLLER_STEREOTYPE = "page controller";
	private static final String VIEW_STEREOTYPE = "view";
	private static final String REST_CONTROLLER_STEREOTYPE = "rest controller";
	private static final String POSSIBLE_VIEW_STEREOTYPE = "possible views";

	private static final String YELLOW = "#FFE4B5";
	private static final String LIGHT_YELLOW = "#FFFFE0";

	private static final String GREEN = "#008000";
	private static final String LIGHT_GREEN = "#90EE90";

	private static final String BLUE = "#0000FF";
	private static final String LIGHT_BLUE = "#F0F8FF";

	@Override
	public PageFlowContext convert(final PageFlow pageFlow) {

		PageFlowContext pageFlowContext = new PageFlowContext();
		List<ContextNode> nodes = new ArrayList<ContextNode>();
		List<ContextEdge> edges = new ArrayList<ContextEdge>();

		Map<String, ContextNode> pageNodesMap = new HashMap<String, ContextNode>();

		int i = 1;
		ContextNode previousPageNode = null;

		for (PageFlowStep pageFlowStep : pageFlow.getPageFlowSteps()) {

			ContextNode pageNode;
			String key = getPageFlowStepKey(pageFlowStep);

			if (pageNodesMap.containsKey(key)) {
			    pageNode = pageNodesMap.get(key);
            }
            else {
                pageNode = getPageContextNode(i++, pageFlowStep);
                pageNodesMap.put(key, pageNode);
            }

			ContextNode pageControllerNode = getPageControllerContextNode(i++, pageFlowStep.getPageController());
			nodes.add(pageControllerNode);
			edges.add(getContextEdge(pageNode, pageControllerNode, PAGE_CONTROLLER_STEREOTYPE));

			if (pageFlowStep.getViews() != null) {
				for (View view : pageFlowStep.getViews()) {
					if (view != null) {
						ContextNode viewNode = getViewContextNode(i++, view);
						nodes.add(viewNode);
						edges.add(getContextEdge(pageNode, viewNode, VIEW_STEREOTYPE));
					}
				}
			}

			if (pageFlowStep.getRestControllers() != null) {
				for (Controller controller : pageFlowStep.getRestControllers()) {
					ContextNode restControllerNode = getRestControllerContextNode(i++, controller);
					nodes.add(restControllerNode);
					edges.add(getContextEdge(pageNode, restControllerNode ,REST_CONTROLLER_STEREOTYPE));
				}
			}

			if (pageFlowStep.getRelatedViews() != null) {
				for (View view : pageFlowStep.getRelatedViews()) {
					if (view != null) {
						ContextNode possibleViewNode = getPossibleViewContextNode(i++, view);
						nodes.add(possibleViewNode);
						edges.add(getContextEdge(pageNode, possibleViewNode, POSSIBLE_VIEW_STEREOTYPE));
					}
				}
			}

			if (previousPageNode != null) {
				edges.add(getContextEdge(previousPageNode, pageNode, PAGE_STEREOTYPE));
			}
			previousPageNode = pageNode;
		}

		pageFlowContext.setNodes(nodes);
		pageFlowContext.getNodes().addAll(new ArrayList<ContextNode>(pageNodesMap.values()));
		pageFlowContext.setEdges(edges);
		return pageFlowContext;
	}

	private ContextEdge getContextEdge(ContextNode from, ContextNode to, String label) {
		ContextEdge edge = new ContextEdge();
		edge.setFrom(from.getId());
		edge.setTo(to.getId());
		edge.setLabel(label);
		EdgeColor edgeColor = new EdgeColor();
		edgeColor.setColor(to.getColor().getBorder());
		edge.setColor(edgeColor);
		return edge;
	}

	private ContextNode getPageContextNode(int id, PageFlowStep pageFlowStep) {
		ContextNode node = new ContextNode();
		node.setId(id + "");
		node.setLabel(getPageFlowStepKey(pageFlowStep));
		node.setStereotype(PAGE_STEREOTYPE);
		node.setType(ContextNodeType.PAGE);
		node.setFont(new Font());
		node.getFont().setSize(24);
		node.setColor(new NodeColor());
		node.getColor().setBackground(LIGHT_GREEN);
		node.getColor().setBorder(GREEN);
		return node;
	}

	private ContextNode getPageControllerContextNode(int id, Controller controller) {
		ContextNode node = new ContextNode();
		node.setId(id + "");
		node.setLabel(controller.getMethodDeclaration() + controller.getMethodBody());
		node.setStereotype(PAGE_CONTROLLER_STEREOTYPE);
		node.setType(ContextNodeType.PAGE_CONTROLLER);
		node.setFont(new Font());
		node.getFont().setAlign("left");
		node.getFont().setSize(14);
		node.setColor(new NodeColor());
		node.getColor().setBackground(LIGHT_BLUE);
		node.getColor().setBorder(BLUE);
		return node;
	}

	private ContextNode getViewContextNode(int id, View view) {
		ContextNode node = new ContextNode();
		node.setId(id + "");
		node.setLabel(view.getContent());
		node.setStereotype(VIEW_STEREOTYPE);
		node.setType(ContextNodeType.VIEW);
		node.setFont(new Font());
		node.getFont().setAlign("left");
		node.getFont().setSize(14);
		node.setColor(new NodeColor());
		node.getColor().setBackground(LIGHT_YELLOW);
		node.getColor().setBorder(YELLOW);
		return node;
	}

	private ContextNode getRestControllerContextNode(int id, Controller controller) {
		ContextNode node = new ContextNode();
		node.setId(id + "");
		node.setLabel(controller.getMethodDeclaration() + controller.getMethodBody());
		node.setStereotype(REST_CONTROLLER_STEREOTYPE);
		node.setType(ContextNodeType.REST_CONTROLLER);
		node.setFont(new Font());
		node.getFont().setAlign("left");
		node.getFont().setSize(14);
		node.setColor(new NodeColor());
		node.getColor().setBackground(LIGHT_BLUE);
		node.getColor().setBorder(BLUE);
		return node;
	}

	private ContextNode getPossibleViewContextNode(int id, View view) {
		ContextNode node = new ContextNode();
		node.setId(id + "");
		node.setLabel(view.getFilePath());
		node.setStereotype(POSSIBLE_VIEW_STEREOTYPE);
		node.setType(ContextNodeType.POSSIBLE_VIEW);
		node.setFont(new Font());
		node.getFont().setSize(14);
		node.setColor(new NodeColor());
		node.getColor().setBackground(LIGHT_YELLOW);
		node.getColor().setBorder(YELLOW);
		return node;
	}

    /**
     * Create String value from object of class PageFlowStep
     * @param pageFlowStep One page of page flow
     * @return String value
     */
    private String getPageFlowStepKey(PageFlowStep pageFlowStep) {
        return pageFlowStep.getRequestPath() + " " + pageFlowStep.getHttpMethod();
    }
}
