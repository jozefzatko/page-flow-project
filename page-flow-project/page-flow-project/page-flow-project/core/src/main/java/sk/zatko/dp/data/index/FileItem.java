package sk.zatko.dp.data.index;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileItem {

    @JsonSerialize
    private String fileName;

    @JsonSerialize
    private String filePath;

}
