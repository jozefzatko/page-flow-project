package sk.zatko.dp.indexer.indexing;

import java.io.File;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.zatko.dp.data.index.FileItem;
import sk.zatko.dp.data.index.FileType;
import sk.zatko.dp.data.index.Index;
import sk.zatko.dp.utils.DirectoryValidator;

/**
 * Application indexing service - implementation
 *
 * @author Jozef Zatko
 */
@Log4j
@Service
public class IndexServiceImpl implements IndexService {

    private String rootDirectory;
    private Index indexConfig;

    private int countOfFiles;

    @Autowired
    private DirectoryValidator directoryValidator;

    @Override
    public Index indexFilesInDirectory(File directory, Index indexConfig) {

        this.rootDirectory = directory.getAbsolutePath();
        this.indexConfig = indexConfig;

        this.countOfFiles = 0;

        if (directory.isDirectory() && directoryValidator.isValid(directory)) {
            processDirectory(directory);
        }

        log.info("Indexed " + this.countOfFiles + " files.");

        return this.indexConfig;
    }

    /**
     * Process all files and directories in directory using recursion
     */
    private void processDirectory(File directory) throws RuntimeException {

        File[] files = directory.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {

            if (file.isFile()) {
                String relativePath = "";

                if (file.getAbsolutePath().startsWith(this.rootDirectory)) {
                    relativePath = file.getAbsolutePath().substring(this.rootDirectory.length() + 1);
                }

                processFile(file, relativePath);

            } else {
                if (file.isDirectory() && directoryValidator.isValid(file)) {
                    processDirectory(file);
                }
            }
        }
    }

    /**
     * Add file to index record if matches criteria
     */
    private void processFile(File file, String relativePath) {

        for (FileType fileType : indexConfig.getFileTypes()) {

            if (file.getName().matches(fileType.getFileNameRegex()) && relativePath.matches(fileType.getFilePathRegex())) {
                fileType.getFileItems().add(new FileItem(file.getName(), relativePath));
                this.countOfFiles++;
            }
        }
    }

}
