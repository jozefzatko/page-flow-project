package sk.zatko.dp.data.pageflows.comparators;

import java.io.Serializable;
import java.util.Comparator;

import sk.zatko.dp.data.pageflows.PageFlow;

/**
 * @author Jozef Zatko
 */
public class PageFlowLengthComparator implements Comparator<PageFlow>, Serializable {

	@Override
	public int compare(final PageFlow pf1, final PageFlow pf2) {

		return Integer.compare(pf1.getPageFlowSteps().size(), pf2.getPageFlowSteps().size()) * -1;
	}
}
