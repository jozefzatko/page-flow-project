package sk.zatko.dp.identifier.seq_mining.mgfsm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import sk.zatko.dp.identifier.seq_mining.mgfsm.model.MGFSMInput;
import sk.zatko.dp.identifier.seq_mining.mgfsm.model.MGFSMInputParams;
import sk.zatko.dp.identifier.seq_mining.mgfsm.model.MGFSMOutput;
import sk.zatko.dp.identifier.seq_mining.mgfsm.model.MGFSMOutputItem;

/**
 * Handle connection and integration with MG-FSM
 *
 * @author Jozef Zatko
 */
@Log4j
@Component
public class MGFSMConnector {

	private static final String MGFSM_OUTPUT_FILE_NAME = "translatedFS";

	private String javaHome = System.getenv("JAVA_HOME").replace("\\", "//");

	public MGFSMOutput MGFSMExecute(final MGFSMInput input) {

		try {
			String command = buildCommand(input);

			log.info("Executing command: " + command);

			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();

			return buildOutput(input.getOutputLocation());

		} catch (Exception e) {
			log.error(e);
		}

		return null;
	}

	/**
	 * Build command for MG-FSM jar application
	 */
	private String buildCommand(final MGFSMInput mgfsmInput) {

		StringBuilder command = new StringBuilder();

		command.append("\"" + this.javaHome + "//bin//java.exe" + "\"");
		command.append(" -jar \"" + mgfsmInput.getAppLocation() + "\"");
		command.append(" -i \"" + mgfsmInput.getInputLocation() + "\"");
		command.append(" -o \"" + mgfsmInput.getOutputLocation() + "\"");

		for (MGFSMInputParams param : mgfsmInput.getParamsList()) {
			command.append(" -" + param.getShortHand() + " " + param.getValue());
		}

		return command.toString();
	}

	/**
	 * Open output file and parse its content into object
	 */
	private MGFSMOutput buildOutput(String outputFilePath) throws IOException {

		MGFSMOutput output = new MGFSMOutput();
		output.setOutputItems(new ArrayList<MGFSMOutputItem>());

		@Cleanup
		BufferedReader reader = new BufferedReader(new FileReader(outputFilePath + MGFSM_OUTPUT_FILE_NAME));

		String line;
		while ((line = reader.readLine()) != null) {

			MGFSMOutputItem outputItem = new MGFSMOutputItem();
			String items[] = line.split("\\t");
			outputItem.setSequence(Arrays.asList(items[0].split(" ")));
			outputItem.setFrequency(Integer.parseInt(items[1]));
			outputItem.setSequenceString(items[0].trim());
			output.getOutputItems().add(outputItem);
		}

		return output;
	}
}
