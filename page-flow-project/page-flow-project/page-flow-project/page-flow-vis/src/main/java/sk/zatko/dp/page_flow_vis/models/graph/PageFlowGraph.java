package sk.zatko.dp.page_flow_vis.models.graph;

import java.util.List;

import lombok.Data;
import sk.zatko.dp.page_flow_vis.models.Edge;
import sk.zatko.dp.page_flow_vis.models.Node;

/**
 * @author Jozef Zatko
 */
@Data
public class PageFlowGraph {

	private List<GraphNode> nodes;
	private List<GraphEdge> edges;
}
