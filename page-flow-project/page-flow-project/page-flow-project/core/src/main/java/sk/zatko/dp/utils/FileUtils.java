package sk.zatko.dp.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component("fileUtils")
public class FileUtils {

    public void saveFile(String path, String content) throws IOException {
        Files.write(Paths.get(path), content.getBytes());
    }

    public void createDirIfNotExist(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

}
