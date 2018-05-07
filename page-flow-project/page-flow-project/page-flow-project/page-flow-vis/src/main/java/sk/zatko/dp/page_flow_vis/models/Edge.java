package sk.zatko.dp.page_flow_vis.models;

import lombok.Data;

/**
 * @author Jozef Zatko
 */
@Data
public class Edge {

	private String from;
	private String to;
	private String label;
	private double weight;
	private int width = 1;
}
