package sk.zatko.dp.identifier.seq_mining.mgfsm.model;

import java.util.List;

import lombok.Data;

/**
 * Encapsulate data required for MG-FSM application
 */
@Data
public class MGFSMInput {

	private String appLocation;
	private String inputLocation;
	private String outputLocation;
	private List<MGFSMInputParams> paramsList;
}
