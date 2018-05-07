package sk.zatko.dp.pfv_gen.generator;

import org.springframework.stereotype.Service;
import sk.zatko.dp.data.controllers.Controller;
import sk.zatko.dp.data.pageflows.PageFlow;
import sk.zatko.dp.data.pageflows.PageFlowStep;
import sk.zatko.dp.data.views.View;

/**
 * Single PageFlow View Generator
 * Java and Markdown implementation
 *
 * @author Jozef Zatko
 */
@Service
public class SinglePageFlowViewGenServiceJavaMarkdownImpl implements SinglePageFlowViewGenService {

	@Override
	public String generateSinglePageFlowView(final PageFlow pageFlow) {

		StringBuilder sb = new StringBuilder("");

		sb.append(getHeader(pageFlow));
		sb.append("\n");

		for (PageFlowStep pageFlowStep : pageFlow.getPageFlowSteps()) {

			sb.append(getIndividualSteps(pageFlowStep));
			sb.append("\n");

			sb.append(getController(pageFlowStep.getPageController()));
			sb.append("\n");

			if (!pageFlowStep.getViews().isEmpty()) {
				sb.append(getViews(pageFlowStep));
			}
			else {
				sb.append(getPossibleViews(pageFlowStep));
			}

			if (pageFlowStep.getRestControllers() != null && !pageFlowStep.getRestControllers().isEmpty()) {
				sb.append(getRestControllers(pageFlowStep));
				sb.append("\n");
			}

		}


		return sb.toString();
	}

	private String getHeader(final PageFlow pageFlow) {

		StringBuilder sb = new StringBuilder("");

		sb.append("# Page Flow\n");

		int i = 1;
		for (PageFlowStep pageFlowStep : pageFlow.getPageFlowSteps()) {

			sb.append(i++);
			sb.append(". ");
			sb.append(pageFlowStep.getRequestPath());
			sb.append(" ");

			sb.append(pageFlowStep.getHttpMethod());
			sb.append("\n");
		}

		return sb.toString();
	}

	private String getIndividualSteps(PageFlowStep pageFlowStep) {

		return "## " + pageFlowStep.getRequestPath() + " " + pageFlowStep.getHttpMethod() + "\n";
	}

	private String getController(Controller controller) {

		StringBuilder sb = new StringBuilder("");

		sb.append("#### ");
		sb.append(controller.getClassName());
		sb.append("\n");

		sb.append(getCode(controller.getMethodDeclaration() + controller.getMethodBody()));

		return sb.toString();
	}

	private String getViews(PageFlowStep pageFlowStep) {

		StringBuilder sb = new StringBuilder("");

		for (View view : pageFlowStep.getViews()) {
			sb.append("#### ");
			sb.append(view.getFilePath());
			sb.append("\n");
			sb.append(getCode(view.getContent()));
			sb.append("\n");
		}
		return sb.toString();
	}

	private String getRestControllers(PageFlowStep pageFlowStep) {

		StringBuilder sb = new StringBuilder("");

		for (Controller controller : pageFlowStep.getRestControllers()) {
		    if (controller != null) {
                sb.append(getController(controller));
            }
		}

		return sb.toString();
	}

	private String getPossibleViews(PageFlowStep pageFlowStep) {

		StringBuilder sb = new StringBuilder("");

		if (pageFlowStep.getRelatedViews() != null && !pageFlowStep.getRelatedViews().isEmpty()) {
			for (View view : pageFlowStep.getRelatedViews()) {
				sb.append("#### ");
				sb.append(view.getFilePath());
				sb.append("\n");
				sb.append(getCode(view.getContent()));
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	private String getCode(String code) {
		return "```\n" + code + "\n```\n";
	}
}
