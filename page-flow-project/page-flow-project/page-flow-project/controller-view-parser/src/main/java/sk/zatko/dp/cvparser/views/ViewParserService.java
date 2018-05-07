package sk.zatko.dp.cvparser.views;

import sk.zatko.dp.data.index.Index;
import sk.zatko.dp.data.views.Views;

/**
 * Parser service for views
 * Generic interface
 *
 * @author Jozef Zatko
 */
public interface ViewParserService {

	/**
	 * Find all page views according to index file
	 *
	 * @param index Index file
	 * @return List of all views
	 */
	Views parseViews(final Index index);

	/**
	 * Find all hypertext links for each view and store it into links attribute
	 *
	 * @param views All application views
	 * @return All application views with set links attribute
	 */
	Views findHypertextLinks(final Views views);

}
