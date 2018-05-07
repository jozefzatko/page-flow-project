package sk.zatko.dp.cvparser.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sk.zatko.dp.data.views.View;

/**
 * Parser of Apache Tiles configuration files
 * Implementation
 *
 * @author Jozef Zatko
 */
@Log4j
@Service
public class TilesConfigParserImpl implements TilesConfigParser {

	private static final String ROOT_ELEMENT = "tiles-definitions";
	private static final String VIEW_DEFINITION_ELEMENT = "definition";
	private static final String LOGICAL_NAME_ATTRIBUTE_NAME = "name";
	private static final String DEFINITION_ATTRIBUTE_NAME = "put-attribute";
	private static final String VIEW_PATH_ATTRIBUTE_NAME = "value";
	private static final String VIEW_TYPE_ATTRIBUTE_NAME = "name";
	private static final String VIEW_TYPE_ATTRIBUTE_VALUE = "content";

	@Value("${source_code.root_directory}")
	public String sourceCodeDirectory;

	@Value("${webapp.path}")
	private String webappPath;

	@Override
	public List<View> parseTilesConfig(String filePath) {

		List<View> views = new ArrayList<View>();

		try {

			File fXmlFile = new File(filePath);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(fXmlFile);
			document.getDocumentElement().normalize();

			if (!ROOT_ELEMENT.equals(document.getDocumentElement().getNodeName())) {
				return views;
			}

			NodeList definitions = document.getElementsByTagName(VIEW_DEFINITION_ELEMENT);

			for (int i=0; i<definitions.getLength(); i++) {
				Node definitionNode = definitions.item(i);

				if (definitionNode.getNodeType() == Node.ELEMENT_NODE) {
					Element definitionElement = (Element) definitionNode;

					View view = new View();
					view.setLogicalName(definitionElement.getAttribute(LOGICAL_NAME_ATTRIBUTE_NAME));

					NodeList attributes = definitionElement.getElementsByTagName(DEFINITION_ATTRIBUTE_NAME);

					for (int j=0; j<attributes.getLength(); j++) {
						Node attributesNode = attributes.item(j);

						if (attributesNode.getNodeType() == Node.ELEMENT_NODE) {
							Element attributesElement = (Element) attributesNode;

							if (VIEW_TYPE_ATTRIBUTE_VALUE.equals(attributesElement.getAttribute(VIEW_TYPE_ATTRIBUTE_NAME))) {
								view.setFilePath(webappPath + "//"
										+ attributesElement.getAttribute(VIEW_PATH_ATTRIBUTE_NAME));

								try {
									@Cleanup
									BufferedReader reader = new BufferedReader(new FileReader(sourceCodeDirectory + "//" + view.getFilePath()));
									StringBuilder sb = new StringBuilder();

									String line;
									while ((line = reader.readLine()) != null) {
										sb.append(line);
										sb.append("\n");
									}
									view.setContent(sb.toString());

								} catch (IOException e) {
									log.error(e);
								}
							}
						}
					}

					if (view.getLogicalName() != null && view.getFilePath() != null) {
						views.add(view);
					}
				}
			}

		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}

		return views;
	}
}
