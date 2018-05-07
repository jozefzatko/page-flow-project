package sk.zatko.dp.cvparser.views;

import java.util.List;

import sk.zatko.dp.data.views.View;

/**
 * Parser of Apache Tiles configuration files
 *
 * @author Jozef Zatko
 */
public interface TilesConfigParser {

	List<View> parseTilesConfig(String filePath);
}
