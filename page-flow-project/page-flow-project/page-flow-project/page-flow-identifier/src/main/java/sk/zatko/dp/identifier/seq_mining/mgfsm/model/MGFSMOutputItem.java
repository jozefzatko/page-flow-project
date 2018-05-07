package sk.zatko.dp.identifier.seq_mining.mgfsm.model;

import java.util.List;

import lombok.Data;

/**
 * One record (one sequence) of MG-FSM output
 */
@Data
public class MGFSMOutputItem {

	String sequenceString;
	private List<String> sequence;
	private int frequency;
}
