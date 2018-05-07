package sk.zatko.dp.identifier.filtering.model;

import java.util.List;

import lombok.Data;
import sk.zatko.dp.data.accesslog.LogItem;

@Data
public class HttpMethodFilter extends AbstractLogFilter {

    private List<String> httpMethods;

    @Override
    protected boolean match(LogItem logItem) {

        for (String httpMethod : this.httpMethods) {

            if (httpMethod.equalsIgnoreCase(logItem.getHttpMethod())) {
                return true;
            }
        }

        return false;
    }
}
