package sk.zatko.dp.identifier.postprocessing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.zatko.dp.data.bundles.Bundle;
import sk.zatko.dp.data.bundles.BundleItem;
import sk.zatko.dp.data.bundles.Bundles;
import sk.zatko.dp.data.controllers.Controllers;
import sk.zatko.dp.data.pageflows.PageFlow;
import sk.zatko.dp.data.pageflows.PageFlowStep;
import sk.zatko.dp.data.pageflows.PageFlows;
import sk.zatko.dp.data.pageflows.comparators.PageFlowLengthComparator;
import sk.zatko.dp.data.pageflows.comparators.PageFlowWeightComparator;

/**
 * Page Flows postprocessing
 * Implementation
 *
 * @author Jozef Zatko
 */
@Service
public class PageFlowsPostprocessingServiceImpl implements PageFlowsPostprocessingService {

	private static final double MINIMAL_WEIGHT_LIMIT = 1.0;

	private int removedSequences = 0;

	@Value("${page_flows.subsequence.diff}")
	private double subsequenceDiff;

	@Override
	public PageFlows postProcess(final PageFlows pageFlows, final Bundles bundles, final Controllers controllers) {

		PageFlows processedPageFlows = pageFlows;

		do {
			processedPageFlows.getPageFlows().sort(new PageFlowLengthComparator());
			processedPageFlows = removeSubsequencesFromFlows(pageFlows);
		} while (this.removedSequences > 0);

		processedPageFlows.getPageFlows().sort(new PageFlowWeightComparator());

		processedPageFlows = deletePageFlowsNotStartingWithGet(processedPageFlows);
		processedPageFlows = deleteRedundantPageFlows(processedPageFlows);
		processedPageFlows = assignBundlesToPageFlows(processedPageFlows, bundles);

        pageFlows.getPageFlows().removeIf(pageFlow -> pageFlow.getWeight() <= MINIMAL_WEIGHT_LIMIT);

		return  processedPageFlows;
	}

	/**
	 * For each sequence in MG-FSM output, we have to identify, if this sequence is a subsequence of any less frequent sequence.
	 * Then, subsequence needs to be removed from the collection, because it is included in longer sequence.
	 * However, it should be only done for sequences with similar weight.
	 * If any subsequence is much more frequent than the sequence itself, it is considered as an individual page flow.
	 * Weight of sequence with removed subsequence is increased.
	 *
	 * @param pageFlows iIdentified page flows by MG-FSM
	 * @return Page flows without subsequences
	 */
	private PageFlows removeSubsequencesFromFlows(PageFlows pageFlows) {

		this.removedSequences = 0;

		if (pageFlows.getPageFlows().size() <= 1) {
			return pageFlows;
		}

		for (PageFlow pageFlow : pageFlows.getPageFlows()) {

			String pageFlowSequence = pageFlow.getSequence();

			Iterator<PageFlow> pageFlowIterator = pageFlows.getPageFlows().iterator();

			while (pageFlowIterator.hasNext()) {

				PageFlow subPageFlow = pageFlowIterator.next();
				String subPageFlowSequence = subPageFlow.getSequence();

				if (pageFlow.getId() == subPageFlow.getId()) {
					continue;
				}

				if (pageFlow.getPageFlowSteps().size() < subPageFlow.getPageFlowSteps().size()) {
					continue;
				}

				if (subPageFlow.getWeight() * subsequenceDiff > pageFlow.getWeight()
						|| subPageFlow.getWeight() / subsequenceDiff < pageFlow.getWeight()) {
					continue;
				}

				if (pageFlowSequence.contains(subPageFlowSequence)) {
					pageFlow.setWeightOfSubsequences(
							pageFlow.getWeightOfSubsequences() + subPageFlow.getWeight() - pageFlow.getWeight()
					);
					System.out.println("Removed id " + subPageFlow.getId());
					pageFlowIterator.remove();
					this.removedSequences++;
					return pageFlows;
				}
			}

		}

		for (PageFlow pageFlow : pageFlows.getPageFlows()) {
			pageFlow.setWeight(pageFlow.getWeight() + pageFlow.getWeightOfSubsequences());
		}
		pageFlows.getPageFlows().sort(new PageFlowWeightComparator());

		return pageFlows;
	}

	/**
	 * Delete page flows, which do not start with page request having GET method
	 */
	private PageFlows deletePageFlowsNotStartingWithGet(final PageFlows pageFlows) {

		pageFlows.getPageFlows().removeIf(pageFlow -> !"GET".equals(pageFlow.getPageFlowSteps().get(0).getHttpMethod()));
		return pageFlows;
	}

	/**
	 * Delete redundant page flows, while keeping the ones with highest weight
	 *
	 * @param pageFlows Application page flows
	 * @return Page flows without redundancy.
	 */
	private PageFlows deleteRedundantPageFlows(final PageFlows pageFlows) {

		HashMap<String, Object> pageFlowHashMap = new LinkedHashMap<String, Object>();

		for (Iterator<PageFlow> pageFlowIterator = pageFlows.getPageFlows().iterator(); pageFlowIterator.hasNext(); ) {
			PageFlow pageFlow = pageFlowIterator.next();

			if (pageFlowHashMap.containsKey(pageFlow.getSequence())) {
				pageFlowIterator.remove();
			} else {
				pageFlowHashMap.put(pageFlow.getSequence(), null);
			}
		}

		return pageFlows;
	}

	/**
	 * Assign bundles (pages per user session) to identified page flows.
	 */
	private PageFlows assignBundlesToPageFlows(PageFlows pageFlows, final Bundles bundles) {

		for (PageFlow pageFlow : pageFlows.getPageFlows()) {

			for (Map.Entry<String, Bundle> entry : bundles.getBundles().entrySet()) {

				Bundle bundle = entry.getValue();

				// bundle contains page flow
				if (bundle.getSequenceOfPageRequests() != null && pageFlow.getSequence() != null
						&& !bundle.getSequenceOfPageRequests().equals(pageFlow.getSequence())
						&& bundle.getSequenceOfPageRequests().contains(pageFlow.getSequence())) {

					int startIndex = bundle.getSequenceOfAllRequests().indexOf(pageFlow.getPageFlowSteps().get(0).getRequestPath());
					// iterate over all occurrences
					while (startIndex >= 0) {

						int madePageFlowSteps = 0;

						for (int i=startIndex; i < bundle.getBundleItems().size() && madePageFlowSteps < pageFlow.getPageFlowSteps().size() ; i++) {

							PageFlowStep pageFlowStep = pageFlow.getPageFlowSteps().get(madePageFlowSteps);
							BundleItem bundleItem = bundle.getBundleItems().get(i);

							if (bundleItem.isRestRequest()) {

								if (pageFlowStep.getAjaxRequests() == null) {
									pageFlowStep.setAjaxRequests(new HashSet<String>());
								}
								pageFlowStep.getAjaxRequests().add(bundleItem.getControllerReqPath());
							}

							if (i == bundle.getBundleItems().size() - 1) {
								continue;
							}

							if (bundle.getBundleItems().get(i+1).isPageRequest()) {
								madePageFlowSteps++;
							}
						}

						startIndex = bundle.getSequenceOfAllRequests().indexOf(pageFlow.getPageFlowSteps().get(0).getRequestPath(), startIndex + 1);
					}

				}
			}
		}

		return pageFlows;
	}

}
