package sk.zatko.dp.data.bundles;

import java.util.HashMap;
import java.util.LinkedHashMap;

import lombok.Data;

/**
 * User Session Pages
 *
 * @author Jozef Zatko
 */
@Data
public class Bundles {

    private HashMap<String, Bundle> bundles;

    public Bundles() {
        this.bundles = new LinkedHashMap<>();
    }

}
