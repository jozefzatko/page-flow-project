package sk.zatko.dp.cvparser.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import sk.zatko.dp.data.controllers.Controller;
import sk.zatko.dp.data.controllers.Controllers;
import sk.zatko.dp.data.index.FileItem;
import sk.zatko.dp.data.index.FileType;
import sk.zatko.dp.data.index.Index;
import sk.zatko.dp.utils.IndexUtils;

/**
 * Service for processing Spring controllers
 *
 * @author Jozef Zatko
 */
@Log4j
@Service
public class ControllerParserServiceSpringMVCImpl implements ControllerParserService {

	private static final String MAPPING_METHOD_REQUEST_PATH_KEY = "value";
	private static final String MAPPING_METHOD_REQUEST_METHOD_KEY = "method";

	private static final String HTTP_METHOD_PREFIX = "RequestMethod.";

	@Autowired
	private IndexUtils indexUtils;

	@Value("${controllers.file_type}")
	private String controllersFileType;

	@Value("${source_code.root_directory}")
	private String sourceCodeRootDir;

	@Override
	public Controllers parseControllers(final Index index) {

		Controllers controllers = new Controllers();
		controllers.setControllers(new LinkedList<Controller>());

		try {
			FileType controllerFileType = indexUtils.getFileType(controllersFileType);
			for (FileItem fileItem : controllerFileType.getFileItems()) {

				File classFile = new File(sourceCodeRootDir + "//" + fileItem.getFilePath());

				if (!classFile.isFile() || !classFile.canRead()) {
					continue;
				}

				try {
					@Cleanup FileInputStream inputStream = new FileInputStream(classFile.getAbsolutePath());
					CompilationUnit cu = JavaParser.parse(inputStream);
					cu.accept(new ClassVisitor(), controllers.getControllers());

				} catch (IOException e) {
					log.error(e);
				}
			}

		} catch (IOException | ProcessingException e) {
			log.error(e);
		}

		return controllers;
	}

	/**
	 * Parsed class visitor
	 */
	private class ClassVisitor extends VoidVisitorAdapter<List<Controller>> {

		public void visit(ClassOrInterfaceDeclaration classOrInterface, List<Controller> foundControllers) {

			if (classOrInterface.isInterface()) {
				return;
			}

			if (classOrInterface.isAnnotationPresent(org.springframework.stereotype.Controller.class)) {
				foundControllers.addAll(parseSpringMVCController(classOrInterface));
			}
		}
	}

	/**
	 * Parse all methods annotated as controllers in Controller class
	 */
	private List<Controller> parseSpringMVCController(ClassOrInterfaceDeclaration springController) {

		List<Controller> controllers = new ArrayList<Controller>();

		StringBuilder controllerRequestMappingBuilder = new StringBuilder("");

		// if controller has @RequestMapping annotation, then store its value into controllerRequestMapping variable
		if (springController.getAnnotationByClass(org.springframework.web.bind.annotation.RequestMapping.class).isPresent()) {
			AnnotationExpr reqMappingAnnotation = springController.getAnnotationByClass(
					org.springframework.web.bind.annotation.RequestMapping.class).get();

			// @RequestMapping("/{something}")
			if (reqMappingAnnotation.isSingleMemberAnnotationExpr()) {

				SingleMemberAnnotationExpr requestMappingAnnotationExpression = reqMappingAnnotation.asSingleMemberAnnotationExpr();
				controllerRequestMappingBuilder.append(requestMappingAnnotationExpression.getMemberValue().toString().replace("\"", ""));

			}
			// @RequestMapping(value="/{something}")
			else {
				NormalAnnotationExpr requestMappingAnnotationExpression = reqMappingAnnotation.asNormalAnnotationExpr();

				for (MemberValuePair mvp : requestMappingAnnotationExpression.getPairs()) {
					if (MAPPING_METHOD_REQUEST_PATH_KEY.equals(mvp.getNameAsString())) {
						controllerRequestMappingBuilder.append(mvp.getValue().toString().replace("\"", ""));
						break;
					}
				}
			}
		}

		// iterate over all methods and process methods with @RequestMapping annotation
		for (MethodDeclaration methodDeclaration : springController.getMethods()) {

			if (methodDeclaration.isAnnotationPresent(org.springframework.web.bind.annotation.RequestMapping.class)) {

				controllers.add(parseControllerFromRequestMappingMethod(
						methodDeclaration, springController.getName().asString(), controllerRequestMappingBuilder.toString()));
			}
		}

		return controllers;
	}

	/**
	 * Extract data from method with @RequestMapping annotation
	 */
	private Controller parseControllerFromRequestMappingMethod(
			final MethodDeclaration methodDeclaration, final String className, final String controllerRequestMapping) {

		Controller controller = new Controller();

		controller.setMethodName(methodDeclaration.getNameAsString());
		controller.setClassName(className);
		controller.setClassFilePath(controllerRequestMapping);

		if (methodDeclaration.isAnnotationPresent(ResponseBody.class)) {
			controller.setRestController(true);
			controller.setPageController(false);
		}
		else {
			controller.setRestController(false);
			controller.setPageController(true);
		}


		controller.setMethodDeclaration(methodDeclaration.getDeclarationAsString());

		if (methodDeclaration.getBody().isPresent()) {
			controller.setMethodBody(methodDeclaration.getBody().get().toString());
		}


		AnnotationExpr reqMappingAnnotation = methodDeclaration.getAnnotationByClass(
				org.springframework.web.bind.annotation.RequestMapping.class).get();

		String requestMapping = "";
		String httpMethod = "";

		if (reqMappingAnnotation.isSingleMemberAnnotationExpr()) {

			SingleMemberAnnotationExpr requestMappingAnnotationExpression = reqMappingAnnotation.asSingleMemberAnnotationExpr();
			requestMapping = requestMappingAnnotationExpression.getMemberValue().toString().replace("\"", "");
		}
		else {

			NormalAnnotationExpr requestMappingAnnotationExpression = reqMappingAnnotation.asNormalAnnotationExpr();

			for (MemberValuePair mvp : requestMappingAnnotationExpression.getPairs()) {
				if (MAPPING_METHOD_REQUEST_PATH_KEY.equals(mvp.getNameAsString())) {
					requestMapping = mvp.getValue().toString().replace("\"", "");
				}
				if (MAPPING_METHOD_REQUEST_METHOD_KEY.equals(mvp.getNameAsString())) {
					httpMethod = mvp.getValue().toString().replace("\"", "");
				}
			}
		}

		if (httpMethod.startsWith(HTTP_METHOD_PREFIX)) {
			httpMethod = httpMethod.substring(HTTP_METHOD_PREFIX.length());
		}

		controller.setRequestMapping(controllerRequestMapping + requestMapping);
		controller.setHttpMethod(httpMethod);

		return controller;
	}

}
