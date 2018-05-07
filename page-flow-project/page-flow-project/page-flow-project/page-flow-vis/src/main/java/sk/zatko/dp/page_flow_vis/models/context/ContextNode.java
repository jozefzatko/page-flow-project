package sk.zatko.dp.page_flow_vis.models.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sk.zatko.dp.page_flow_vis.models.Node;
import sk.zatko.dp.page_flow_vis.models.NodeColor;

/**
 * @author Jozef Zatko
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class ContextNode extends Node {

	private NodeColor color;
	private String stereotype;
	private ContextNodeType type;
}
