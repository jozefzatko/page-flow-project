package sk.zatko.dp.identifier.filtering.model;

import java.util.List;

import lombok.Data;

@Data
public class AccessLogFilter {

    private List<HttpMethodFilter> httpMethodFilters;
    private List<HttpRequestPathRegexFilter> httpRequestPathRegexFilters;
    private List<ResponseCodeFilter> responseCodeFilters;

}
