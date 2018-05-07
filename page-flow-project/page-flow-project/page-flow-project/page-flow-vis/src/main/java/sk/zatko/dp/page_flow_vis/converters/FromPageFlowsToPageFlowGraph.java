package sk.zatko.dp.page_flow_vis.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import sk.zatko.dp.data.pageflows.PageFlow;
import sk.zatko.dp.data.pageflows.PageFlowStep;
import sk.zatko.dp.data.pageflows.PageFlows;
import sk.zatko.dp.page_flow_vis.models.*;
import sk.zatko.dp.page_flow_vis.models.graph.GraphEdge;
import sk.zatko.dp.page_flow_vis.models.graph.GraphNode;
import sk.zatko.dp.page_flow_vis.models.graph.PageFlowGraph;

/**
 * Convert PageFlows object to PageFlowGraph object
 *
 * @author Jozef Zatko
 */
@Component
public class FromPageFlowsToPageFlowGraph implements Converter<PageFlows,PageFlowGraph> {

    private static final int FONT_SIZE = 16;
    private static final String GREY = "#808080";
    private static final String LIGHT_GREY = "#E0E0E0";

    private static final String BLUE = "#0000FF";
    private static final String LIGHT_BLUE = "#F0F8FF";

    private static final String RED = "#FF0000";
    private static final String LIGHT_RED = "#FFA07A";

    private static final String PURPLE = "#FF00BF";
    private static final String LIGHT_PURPLE = "#DDA0DD";

    private static Map<String, NodeColor> nodeColorMap = new HashMap<String, NodeColor>();
    static {
        NodeColor purple = new NodeColor();
        purple.setBackground(LIGHT_PURPLE);
        purple.setBorder(PURPLE);

        NodeColor blue = new NodeColor();
        blue.setBackground(LIGHT_BLUE);
        blue.setBorder(BLUE);

        NodeColor red = new NodeColor();
        red.setBackground(LIGHT_RED);
        red.setBorder(RED);

        nodeColorMap.put("/ GET", purple);

        nodeColorMap.put("/invite GET", blue);
        nodeColorMap.put("/invite/twitter GET", blue);
        nodeColorMap.put("/invite/twitter POST", blue);

        nodeColorMap.put("/develop/apps GET", red);
        nodeColorMap.put("/develop/apps/new GET", red);
        nodeColorMap.put("/develop/apps POST", red);
        nodeColorMap.put("/develop/apps/{slug} GET", red);
    }

    private static Map<String, EdgeColor> edgeColorMap = new HashMap<String, EdgeColor>();
    static {
        EdgeColor blue = new EdgeColor();
        blue.setColor(BLUE);

        EdgeColor red = new EdgeColor();
        red.setColor(RED);

        edgeColorMap.put("/ GET /invite GET" , blue);
        edgeColorMap.put("/invite GET /invite/twitter GET" , blue);
        edgeColorMap.put("/invite/twitter GET /invite/twitter POST" , blue);
        edgeColorMap.put("/invite/twitter POST /invite/twitter GET" , blue);

        edgeColorMap.put("/ GET /develop/apps GET" , red);
        edgeColorMap.put("/develop/apps GET /develop/apps/new GET" , red);
        edgeColorMap.put("/develop/apps/new GET /develop/apps POST" , red);
        edgeColorMap.put("/develop/apps POST /develop/apps/{slug} GET" , red);
    }

    @Nullable
    @Override
    public PageFlowGraph convert(final PageFlows pageFlows) {

        PageFlowGraph pageFlowGraph = new PageFlowGraph();
        List<GraphNode> nodes = new ArrayList<GraphNode>();
        List<GraphEdge> edges = new ArrayList<GraphEdge>();

        Map<String, GraphNode> nodesMap = new HashMap<String, GraphNode>();

        for (PageFlow pageFlow : pageFlows.getPageFlows()) {
            for (PageFlowStep pageFlowStep : pageFlow.getPageFlowSteps()) {

                String pageFlowStepKey = getPageFlowStepKey(pageFlowStep);

                if (!nodesMap.containsKey(pageFlowStepKey)) {
                    GraphNode node = createPageFlowGraphNode(pageFlowStepKey);

                    if (nodeColorMap.containsKey(pageFlowStepKey)) {
                        node.setColor(nodeColorMap.get(pageFlowStepKey));
                    }

                    nodesMap.put(pageFlowStepKey, node);
                }
            }
        }

        Map<String, GraphEdge> edgesMap = new HashMap<String, GraphEdge>();

        for (PageFlow pageFlow : pageFlows.getPageFlows()) {
            for (int i=0; i<pageFlow.getPageFlowSteps().size()-1; i++) {

                PageFlowStep current = pageFlow.getPageFlowSteps().get(i);
                PageFlowStep next = pageFlow.getPageFlowSteps().get(i+1);

                String edgeKey = getPageFlowStepKey(current) + " " + getPageFlowStepKey(next);

                if (edgesMap.containsKey(edgeKey)) {
                    GraphEdge edge = edgesMap.get(edgeKey);
                    edge.setWeight(edge.getWeight() + pageFlow.getWeight());
                }
                else {
                    GraphNode source = nodesMap.get(getPageFlowStepKey(current));
                    GraphNode target = nodesMap.get(getPageFlowStepKey(next));

                    GraphEdge edge = new GraphEdge();
                    edge.setFrom(source.getId());
                    edge.setTo(target.getId());
                    edge.setWeight(pageFlow.getWeight());

                    if (edgeColorMap.containsKey(edgeKey)) {
                        edge.setColor(edgeColorMap.get(edgeKey));
                        edge.setWidth(2);
                    }
                    else {
                        EdgeColor edgeColor = new EdgeColor();
                        edgeColor.setColor(GREY);
                        edge.setColor(edgeColor);
                    }

                    edgesMap.put(edgeKey, edge);
                }
            }
        }

        for (Map.Entry<String, GraphNode> entry : nodesMap.entrySet()) {
            if (!GREY.equals(entry.getValue().getColor().getBackground())) {
                nodes.add(entry.getValue());
            }
        }

        for (Map.Entry<String, GraphEdge> entry : edgesMap.entrySet()) {

            if (entry.getValue().getWeight() >= 1.0) {

                Double d = entry.getValue().getWeight();
                Integer i = d.intValue();

                entry.getValue().setLabel(i + "");
                edges.add(entry.getValue());
            }
        }

        pageFlowGraph.setNodes(nodes);
        pageFlowGraph.setEdges(edges);
        return pageFlowGraph;
    }

    private GraphNode createPageFlowGraphNode(final String pageFlowStepKey) {
        GraphNode node = new GraphNode();
        node.setId(pageFlowStepKey);
        node.setLabel(pageFlowStepKey);
        node.setFont(new Font());
        node.getFont().setSize(FONT_SIZE);
        node.setColor(new NodeColor());
        node.getColor().setBackground(LIGHT_GREY);
        node.getColor().setBorder(GREY);
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
