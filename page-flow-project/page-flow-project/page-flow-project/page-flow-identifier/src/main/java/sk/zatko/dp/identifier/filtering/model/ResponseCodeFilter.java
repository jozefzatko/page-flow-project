package sk.zatko.dp.identifier.filtering.model;

import java.util.List;

import lombok.Data;
import sk.zatko.dp.data.accesslog.LogItem;

@Data
public class ResponseCodeFilter extends AbstractLogFilter {

    private List<Integer> responseCodes;

    @Override
    protected boolean match(LogItem logItem) {

        for (Integer responseCode : this.responseCodes) {

            if (responseCode == logItem.getResponseCode()) {
                return true;
            }
        }

        return false;
    }
}
