package sk.zatko.dp.data.pageflows.comparators;

import java.io.Serializable;
import java.util.Comparator;

import sk.zatko.dp.data.pageflows.PageFlow;

/**
 * @author Jozef Zatko
 */
public class PageFlowWeightComparator implements Comparator<PageFlow>, Serializable {

	@Override
	public int compare(final PageFlow pf1, final PageFlow pf2) {

		return Double.compare(pf1.getWeight(), pf2.getWeight()) * -1;
	}
}
