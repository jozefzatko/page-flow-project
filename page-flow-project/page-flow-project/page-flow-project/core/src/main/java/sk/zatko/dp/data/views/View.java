package sk.zatko.dp.data.views;

import java.util.Set;

import lombok.Data;

/**
 * @author Jozef Zatko
 */
@Data
public class View {

	private String logicalName;
	private String filePath;
	private String content;
	private Set<String> links;
}
