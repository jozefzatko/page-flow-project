package sk.zatko.dp.page_flow_vis.models.graph;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sk.zatko.dp.page_flow_vis.models.Edge;
import sk.zatko.dp.page_flow_vis.models.EdgeColor;

@Data
@EqualsAndHashCode(callSuper=true)
public class GraphEdge extends Edge {

    private EdgeColor color;
}
