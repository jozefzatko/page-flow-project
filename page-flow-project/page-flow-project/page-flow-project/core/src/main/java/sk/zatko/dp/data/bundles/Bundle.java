package sk.zatko.dp.data.bundles;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Bundle {

    private String userId;
    private String sequenceOfPageRequests;
    private String sequenceOfAllRequests;
    private List<BundleItem> bundleItems;

    public Bundle() {
        this.bundleItems = new ArrayList<BundleItem>();
    }

    public void createBundles() {


    }

}
