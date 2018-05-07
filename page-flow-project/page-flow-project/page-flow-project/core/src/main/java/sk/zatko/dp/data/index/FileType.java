package sk.zatko.dp.data.index;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class FileType {

    @JsonSerialize @JsonDeserialize
    private String fileTypeName;

    @JsonSerialize @JsonDeserialize
    private String fileTypeDescription;

    @JsonDeserialize
    private String fileNameRegex;

    @JsonDeserialize
    private String filePathRegex;

    @JsonSerialize
    private List<FileItem> fileItems;

    public FileType() {
        this.fileItems = new ArrayList<FileItem>();
    }
}
