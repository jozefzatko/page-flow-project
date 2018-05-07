package sk.zatko.dp.page_flow_vis.models.context;

import java.util.List;

import lombok.Data;

/**
 * @author Jozef Zatko
 */
@Data
public class PageFlowContext {

	private List<ContextNode> nodes;
	private List<ContextEdge> edges;
}
