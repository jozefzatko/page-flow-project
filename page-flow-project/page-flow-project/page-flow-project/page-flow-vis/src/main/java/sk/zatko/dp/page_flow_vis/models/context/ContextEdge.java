package sk.zatko.dp.page_flow_vis.models.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sk.zatko.dp.page_flow_vis.models.Edge;
import sk.zatko.dp.page_flow_vis.models.EdgeColor;

/**
 * @author Jozef Zatko
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class ContextEdge extends Edge {

    private EdgeColor color;
}
