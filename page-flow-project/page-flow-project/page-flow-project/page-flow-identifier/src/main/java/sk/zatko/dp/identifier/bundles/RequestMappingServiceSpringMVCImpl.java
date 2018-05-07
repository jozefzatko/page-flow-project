package sk.zatko.dp.identifier.bundles;

import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Service;
import sk.zatko.dp.data.bundles.Bundle;
import sk.zatko.dp.data.bundles.BundleItem;
import sk.zatko.dp.data.bundles.Bundles;
import sk.zatko.dp.data.controllers.Controller;
import sk.zatko.dp.data.controllers.Controllers;

/**
 * Map request mapping specified in controllers into bundles
 * Spring MVC request mapping implementation
 *
 * @author Jozef Zatko
 */
@Service
public class RequestMappingServiceSpringMVCImpl implements RequestMappingService {

    @Override
    public Bundles mapRequestMapping(final Bundles bundles, final Controllers controllers) {

        for (Controller pageController : controllers.getControllers()) {

            String controllerReqPathRegex = fromSpringPathToRegex(pageController.getRequestMapping());

            for (Iterator<Map.Entry<String, Bundle>> bundleIterator = bundles.getBundles().entrySet().iterator(); bundleIterator.hasNext(); ) {
                Map.Entry<String, Bundle> entry = bundleIterator.next();
                Bundle bundle = entry.getValue();

                for (BundleItem bundleItem : bundle.getBundleItems()) {
                    if (bundleItem.getControllerReqPath() == null && bundleItem.getUrlRequestPath().equalsIgnoreCase(controllerReqPathRegex)) {

                        if (!pageController.getHttpMethod().isEmpty() && !pageController.getHttpMethod().equalsIgnoreCase(bundleItem.getHttpMethod())) {
                            continue;
                        }

                        bundleItem.setControllerReqPath(pageController.getRequestMapping());

                        if (pageController.isPageController()) {
                            bundleItem.setPageRequest(true);
                        } else {
                            bundleItem.setPageRequest(false);
                        }

                        if (pageController.isRestController()) {
                            bundleItem.setRestRequest(true);
                        } else {
                            bundleItem.setRestRequest(false);
                        }
                    }
                }

                for (BundleItem bundleItem : bundle.getBundleItems()) {
                    if (bundleItem.getControllerReqPath() == null && bundleItem.getUrlRequestPath().matches(controllerReqPathRegex)) {

                        if (!pageController.getHttpMethod().isEmpty() && !pageController.getHttpMethod().equalsIgnoreCase(bundleItem.getHttpMethod())) {
                            continue;
                        }

                        bundleItem.setControllerReqPath(pageController.getRequestMapping());

                        if (pageController.isRestController()) {
                            bundleItem.setRestRequest(true);
                            bundleItem.setPageRequest(false);
                        } else {
                            bundleItem.setRestRequest(false);
                            bundleItem.setPageRequest(true);
                        }
                    }
                }
            }

        }

        for(Iterator<Map.Entry<String, Bundle>> bundleIterator = bundles.getBundles().entrySet().iterator(); bundleIterator.hasNext(); ) {

            Map.Entry<String, Bundle> entry = bundleIterator.next();
            Bundle bundle = entry.getValue();

            StringBuilder pageSeqBuilder = new StringBuilder("");
            StringBuilder allSeqBuilder = new StringBuilder("");

            Iterator<BundleItem> bundleItemIterator = bundle.getBundleItems().iterator();

            while (bundleItemIterator.hasNext()) {

                BundleItem bundleItem = bundleItemIterator.next();

                if (bundleItem.getControllerReqPath() == null) {
                    bundleItemIterator.remove();
                    continue;
                }

                String bundleItemString = bundleItem.getControllerReqPath() + "&" + bundleItem.getHttpMethod() + " ";

                allSeqBuilder.append(bundleItemString);

                if (bundleItem.isPageRequest()) {
                    pageSeqBuilder.append(bundleItemString);
                }
            }

            if (bundle.getBundleItems().isEmpty()) {
                bundleIterator.remove();
            }

            bundle.setSequenceOfPageRequests(pageSeqBuilder.toString().trim());
            bundle.setSequenceOfAllRequests(allSeqBuilder.toString().trim());
        }

        return bundles;
    }

    /**
     * Convert Spring MVC request path expression into regex
     *
     * @param reqPath Spring MVC controller request path
     * @return Regex from request controller path
     */
    private String fromSpringPathToRegex(String reqPath) {

        return reqPath.replace("@self", "{username}").replaceAll("\\{[^\\{\\}\\/]*\\}", "[^\\/]*");
    }
}
