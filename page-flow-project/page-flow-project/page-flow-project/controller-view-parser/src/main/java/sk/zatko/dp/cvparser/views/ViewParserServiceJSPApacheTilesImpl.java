package sk.zatko.dp.cvparser.views;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.zatko.dp.data.index.FileItem;
import sk.zatko.dp.data.index.FileType;
import sk.zatko.dp.data.index.Index;
import sk.zatko.dp.data.views.View;
import sk.zatko.dp.data.views.Views;
import sk.zatko.dp.utils.IndexUtils;

/**
 * Implementation of ViewParserService for JSP and Apache tiles
 *
 * @author Jozef Zatko
 */
@Log4j
@Service
public class ViewParserServiceJSPApacheTilesImpl implements ViewParserService {

	private static final List<String> HYPERTEXT_LINK_REGEX_PATTERNS = new ArrayList<String>();

	static {

		HYPERTEXT_LINK_REGEX_PATTERNS.add("<.:url value=\"(\\S+)\" ?\\/>\"");
		HYPERTEXT_LINK_REGEX_PATTERNS.add("<a href=\"<.:url value=\"(\\S+)\" ?\\/>\"");
		HYPERTEXT_LINK_REGEX_PATTERNS.add("<a href=\"(\\S+)\" ?\\/>\"");
	}



	@Autowired
	private IndexUtils indexUtils;

	@Autowired
	private TilesConfigParser tilesConfigParser;

	@Value("${tiles.file_type}")
	private String tilesFileType;

	@Value("${source_code.root_directory}")
	private String sourceCodeRootDir;

	@Override
	public Views parseViews(final Index index) {

		Views views = new Views();
		views.setViews(new ArrayList<View>());

		try {
			FileType fileType = indexUtils.getFileType(tilesFileType);

			for (FileItem tilesItem : fileType.getFileItems()) {

				views.getViews().addAll(
						tilesConfigParser.parseTilesConfig(sourceCodeRootDir + "//" + tilesItem.getFilePath())
				);
			}

		} catch (IOException | ProcessingException e) {
			e.printStackTrace();
		}
		return views;
	}

	@Override
	public Views findHypertextLinks(final Views views) {

		List<Pattern> linkPatterns = new ArrayList<Pattern>(HYPERTEXT_LINK_REGEX_PATTERNS.size());
		for (String pattern : HYPERTEXT_LINK_REGEX_PATTERNS) {
			linkPatterns.add(Pattern.compile(pattern));
		}

		for (View view : views.getViews()) {

			view.setLinks(new HashSet<>());

			try {

				@Cleanup
				BufferedReader reader = new BufferedReader(new FileReader(sourceCodeRootDir + "//" + view.getFilePath()));

				String line;
				while ((line = reader.readLine()) != null) {

					for (Pattern pattern : linkPatterns) {

						Matcher linkMatcher = pattern.matcher(line);
						while (linkMatcher.find()) {

							String hypertextLink = linkMatcher.group(1);
							view.getLinks().add(hypertextLink);
						}
					}
				}

			} catch (IOException e) {
				log.error(e);
			}
		}

		return views;
	}

}
