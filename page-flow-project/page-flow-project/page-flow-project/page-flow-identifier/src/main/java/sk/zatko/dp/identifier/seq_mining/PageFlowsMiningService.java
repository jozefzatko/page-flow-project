package sk.zatko.dp.identifier.seq_mining;

import sk.zatko.dp.data.bundles.Bundles;
import sk.zatko.dp.data.pageflows.PageFlows;

/**
 * Service to apply sequence identifying algorithms to get page flows from sequences of request paths
 * Generic interface
 */
public interface PageFlowsMiningService {

    /**
     * Get page flows from sequences of request paths
     *
     * @param bundles User session pages created from access logs
     * @return Page flows object - identified page flows
     */
    PageFlows getRawPageFlows(Bundles bundles);
}
