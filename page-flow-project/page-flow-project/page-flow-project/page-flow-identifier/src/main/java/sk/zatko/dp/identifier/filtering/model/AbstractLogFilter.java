package sk.zatko.dp.identifier.filtering.model;

import lombok.Data;
import sk.zatko.dp.data.accesslog.LogItem;

@Data
public abstract class AbstractLogFilter {

    private boolean includeMatched;
    private boolean excludeMatched;

    protected abstract boolean match(LogItem logItem);

    public boolean isIncluded(LogItem logItem) {

        boolean isMatch = match(logItem);

        if (match(logItem) && this.includeMatched) {
            return true;
        }

        if (!match(logItem) || !this.excludeMatched) {
            return true;
        }

        return false;
    }
}
