package sk.zatko.dp.pfv_gen.generator;

import sk.zatko.dp.data.pageflows.PageFlow;

/**
 * Single PageFlow View Generator
 * Generic interface
 *
 * @author Jozef Zatko
 */
public interface SinglePageFlowViewGenService {

	/**
	 * Generate page flow view in the text form based on PageFlow object
	 *
	 * @param pageFlow PageFlow object
	 * @return page flow view in the text form
	 */
	String generateSinglePageFlowView(PageFlow pageFlow);
}
