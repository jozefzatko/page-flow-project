package sk.zatko.dp.data.index;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class Index {

    @JsonSerialize @JsonDeserialize
    private List<FileType> fileTypes;

    public Index() {
        this.fileTypes = new ArrayList<FileType>();
    }
}
