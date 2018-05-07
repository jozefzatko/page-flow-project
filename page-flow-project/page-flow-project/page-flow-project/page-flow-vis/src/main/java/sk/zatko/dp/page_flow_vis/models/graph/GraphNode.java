package sk.zatko.dp.page_flow_vis.models.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sk.zatko.dp.page_flow_vis.models.Node;
import sk.zatko.dp.page_flow_vis.models.NodeColor;

@Data
@EqualsAndHashCode(callSuper=true)
public class GraphNode extends Node {

    private NodeColor color;
}
