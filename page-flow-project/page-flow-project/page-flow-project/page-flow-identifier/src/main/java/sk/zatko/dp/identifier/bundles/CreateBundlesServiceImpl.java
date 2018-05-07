package sk.zatko.dp.identifier.bundles;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.stereotype.Service;
import sk.zatko.dp.data.accesslog.AccessLog;
import sk.zatko.dp.data.accesslog.LogItem;
import sk.zatko.dp.data.bundles.Bundle;
import sk.zatko.dp.data.bundles.BundleItem;
import sk.zatko.dp.data.bundles.Bundles;
import sk.zatko.dp.data.bundles.QueryParam;

/**
 * Service creating bundles - access log items aggregated according to users
 * Implementation
 *
 * @author Jozef Zatko
 */
@Log4j
@Service
public class CreateBundlesServiceImpl implements CreateBundlesService {

	@Override
	public Bundles createBundledLog(final AccessLog accessLog) {

		Bundles filteredLog = new Bundles();

		for (LogItem logItem : accessLog.getLogs()) {

			String userId = logItem.getUserId();

			BundleItem bundleItem = new BundleItem();
			bundleItem.setTimestamp(logItem.getTimestamp());
			bundleItem.setUrl(logItem.getHttpRequestPath());
			bundleItem.setUrlRequestPath(parseRequestPath(bundleItem.getUrl()));
			bundleItem.setUrlQueryParams(parseQueryParams(bundleItem.getUrl()));
			bundleItem.setHttpMethod(logItem.getHttpMethod());

			if (filteredLog.getBundles().containsKey(userId)) {

				filteredLog.getBundles().get(userId).getBundleItems().add(bundleItem);

			} else {

				Bundle bundle = new Bundle();
				bundle.setUserId(userId);
				bundle.getBundleItems().add(bundleItem);

				filteredLog.getBundles().put(userId, bundle);
			}
		}
		return filteredLog;
	}

	/**
	 * Get request path from URL
	 *
	 * @param url Page URL
	 * @return Request path
	 */
	private String parseRequestPath(final String url) {

		if (url.contains("?")) {
			return url.split("\\?")[0];
		}
		return url;
	}

	/**
	 * Parse query params from URL
	 *
	 * @param url URL
	 * @return List of query params
	 */
	private List<QueryParam> parseQueryParams(final String url) {

		List<QueryParam> params = new ArrayList<QueryParam>();

		try {
			List<NameValuePair> queryPairs = URLEncodedUtils.parse(new URI(url), Charset.defaultCharset());

			for (NameValuePair pair : queryPairs) {
				QueryParam queryParam = new QueryParam();
				queryParam.setName(pair.getName());
				queryParam.setValue(pair.getValue());
				params.add(queryParam);
			}

		} catch (URISyntaxException e) {
			log.error(e);
		}
		return params;
	}

}
