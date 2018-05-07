package sk.zatko.dp.identifier.filtering.model;

import lombok.Data;
import sk.zatko.dp.data.accesslog.LogItem;

@Data
public class HttpRequestPathRegexFilter extends AbstractLogFilter {

    private String requestPathRegex;

    @Override
    protected boolean match(LogItem logItem) {

        if (logItem.getHttpRequestPath().matches(this.requestPathRegex)) {
            return true;
        }
        return false;
    }
}
