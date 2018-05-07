package sk.zatko.dp.identifier.seq_mining.mgfsm.model;

import java.util.List;

import lombok.Data;

/**
 * Data object encapsulating output from MG-FSM application
 */
@Data
public class MGFSMOutput {

	List<MGFSMOutputItem> outputItems;
}
