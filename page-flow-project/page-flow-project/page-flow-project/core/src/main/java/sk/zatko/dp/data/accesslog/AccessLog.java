package sk.zatko.dp.data.accesslog;

import java.util.List;

import lombok.Data;

/**
 * Model for standardized format of access log
 *
 * @author Jozef Zatko
 */
@Data
public class AccessLog {

    private List<LogItem> logs;
}
