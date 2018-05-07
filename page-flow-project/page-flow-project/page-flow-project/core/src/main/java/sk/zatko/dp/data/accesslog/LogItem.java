package sk.zatko.dp.data.accesslog;

import lombok.Data;
import org.joda.time.DateTime;

/**
 * Model for standardized format of one access log item - http request
 *
 * @author Jozef Zatko
 */
@Data
public class LogItem {

    private String clientIpAddress;
    private DateTime timestamp;
    private String httpMethod;
    private String httpRequestPath;
    private int responseCode;
    private String userSessionId;
    private String userAgent;

    /**
     * Return hash of clientIpAddress, userSessionId and userAgent
     */
    public String getUserId() {

        long result = 17;

        result = 23 * result + this.clientIpAddress.hashCode();
        result = 23 * result + this.userSessionId.hashCode();
        result = 23 * result + this.userAgent.hashCode();

        return String.valueOf(result);
    }

}
