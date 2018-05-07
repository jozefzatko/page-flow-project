package sk.zatko.dp.utils;

import java.io.File;

import org.springframework.stereotype.Component;

@Component("directoryValidator")
public class DirectoryValidator {

    public boolean isValid(File directory) throws IllegalArgumentException {

        if (directory == null) {
            throw new IllegalArgumentException("Directory should not be null.");
        }
        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory does not exist: " + directory);
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Is not a directory: " + directory);
        }

        return true;
    }

    public boolean isReadable(File directory) throws  IllegalArgumentException {

        isValid(directory);

        if (!directory.canRead()) {
            throw new IllegalArgumentException("Directory cannot be read: " + directory);
        }

        return true;
    }

}
