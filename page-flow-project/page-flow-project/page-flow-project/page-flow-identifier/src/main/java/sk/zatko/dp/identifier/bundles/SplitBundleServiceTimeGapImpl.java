package sk.zatko.dp.identifier.bundles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.zatko.dp.data.bundles.Bundle;
import sk.zatko.dp.data.bundles.BundleItem;
import sk.zatko.dp.data.bundles.Bundles;


/**
 * Split bundle into multiple ones
 * Split according to time gap
 *
 * @author Jozef Zatko
 */
@Service
public class SplitBundleServiceTimeGapImpl implements SplitBundleService {

	private static final String BUNDLE_DELIMITER = "-";

	@Value("${bundles.split_time}")
	private int splitTimeInSecs;

	@Override
	public Bundles splitBundles(final Bundles bundledLog) {

		HashMap<String, Bundle> tempBundles = new LinkedHashMap<String, Bundle>();

		for (Map.Entry<String, Bundle> entry : bundledLog.getBundles().entrySet()) {

			List<Bundle> bundles = splitSingleBundle(entry.getValue(), this.splitTimeInSecs);

			// bundle was not split
			if (bundles.size() == 1) {
				tempBundles.put(entry.getKey(), entry.getValue());
				continue;
			}

			// bundle was split to multiple parts
			for (int i=0; i<bundles.size(); i++) {
				Bundle bundle = bundles.get(i);
				tempBundles.put(bundle.getUserId() + BUNDLE_DELIMITER + i, bundle);
			}
		}

		bundledLog.setBundles(tempBundles);
		return bundledLog;
	}

	/**
	 * Split single bundle according to time between requests
	 *
	 * @param bundle User session page created from access logs
	 * @param splitTimeInSecs time between requests in seconds
	 * @return List of bundles
	 */
	private List<Bundle> splitSingleBundle(Bundle bundle, int splitTimeInSecs) {

		List<Bundle> bundles = new ArrayList<Bundle>();
		List<BundleItem> bundleItemList = new ArrayList<BundleItem>();

		for (int i=0; i<bundle.getBundleItems().size()-1; i++) {

			bundleItemList.add(bundle.getBundleItems().get(i));

			DateTime current = bundle.getBundleItems().get(i).getTimestamp();
			DateTime next = bundle.getBundleItems().get(i+1).getTimestamp();

			int timeDiffInSecs = Seconds.secondsBetween(current, next).getSeconds();

			if (timeDiffInSecs >= splitTimeInSecs) {

				Bundle bundleSplit = new Bundle();
				bundleSplit.setUserId(bundle.getUserId());
				bundleSplit.getBundleItems().addAll(bundleItemList);
				bundles.add(bundleSplit);

				bundleItemList.clear();
			}
		}

		bundleItemList.add(bundle.getBundleItems().get(bundle.getBundleItems().size()-1));

		Bundle bundleSplit = new Bundle();
		bundleSplit.setUserId(bundle.getUserId());
		bundleSplit.getBundleItems().addAll(bundleItemList);
		bundles.add(bundleSplit);

		return bundles;
	}
}
