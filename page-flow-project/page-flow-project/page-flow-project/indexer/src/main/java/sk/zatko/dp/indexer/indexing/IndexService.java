package sk.zatko.dp.indexer.indexing;

import java.io.File;

import sk.zatko.dp.data.index.Index;

/**
 * Application indexing service
 *
 * @author Jozef Zatko
 */
public interface IndexService {

	/**
	 * Index all files and directories in directory using recursion
	 *
	 * @param directory Root application directory
	 * @param indexConfig Indexing configuration object
	 * @return Index object
	 */
	Index indexFilesInDirectory(File directory, Index indexConfig);

}