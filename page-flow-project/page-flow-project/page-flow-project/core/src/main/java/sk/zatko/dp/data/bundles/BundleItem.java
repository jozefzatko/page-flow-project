package sk.zatko.dp.data.bundles;

import java.util.List;

import lombok.Data;
import org.joda.time.DateTime;

/**
 * Bundles = Pages per user session
 */
@Data
public class BundleItem {

    private DateTime timestamp;
    private String url;
    private String urlRequestPath;
    private List<QueryParam> urlQueryParams;
    private String controllerReqPath;
    private String httpMethod;
    private boolean pageRequest;
    private boolean restRequest;
}
