package sk.zatko.dp.identifier.seq_mining;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.zatko.dp.data.bundles.Bundle;
import sk.zatko.dp.data.bundles.BundleItem;
import sk.zatko.dp.data.bundles.Bundles;
import sk.zatko.dp.data.pageflows.PageFlow;
import sk.zatko.dp.data.pageflows.PageFlowStep;
import sk.zatko.dp.data.pageflows.PageFlows;
import sk.zatko.dp.data.pageflows.comparators.PageFlowWeightComparator;
import sk.zatko.dp.identifier.seq_mining.mgfsm.MGFSMConnector;
import sk.zatko.dp.identifier.seq_mining.mgfsm.model.MGFSMInput;
import sk.zatko.dp.identifier.seq_mining.mgfsm.model.MGFSMOutput;
import sk.zatko.dp.identifier.seq_mining.mgfsm.model.MGFSMOutputItem;
import sk.zatko.dp.utils.FileUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * Service to apply sequence identifying algorithms to get page flows from sequences of request paths
 * Implementation using MG-FSM sequence mining implementation
 */
@Log4j
@Service
public class PageFlowsMiningServiceMGFSMImpl implements PageFlowsMiningService {

    private final Gson GSON = Converters.registerDateTime(new GsonBuilder()).setPrettyPrinting().create();

    @Autowired
    private MGFSMConnector mgfsmConnector;

    @Autowired
    private FileUtils fileUtils;

    @Value("${page_flows.skip_rest_requests}")
    private boolean skipRestRequests;

    @Value("${mgfsm_config.file_path}")
    private String mgfsmConfigFilePath;

    private String mgfsmPath;
    private String tmpPath;
    private String reqSequencesFilePath;
    private String rawPageFlowsFilePath;

    @PostConstruct
    public void init() {

        String projectPath = System.getProperty("user.dir");
        this.mgfsmPath = projectPath + "//mgfsm.jar";
        this.tmpPath = projectPath + "//tmp";
        this.reqSequencesFilePath = this.tmpPath + "//rs.txt";
        this.rawPageFlowsFilePath = this.tmpPath + "//raw_page_flows//";
    }

    @Override
    public PageFlows getRawPageFlows(final Bundles bundles) {

        PageFlows pageFlows = new PageFlows();

        try {

            String reqPathSequences = generateReqPathSequences(bundles);

            MGFSMInput input = getMGFSMInput();
            input.setAppLocation(mgfsmPath);
            input.setInputLocation(reqSequencesFilePath);
            input.setOutputLocation(rawPageFlowsFilePath);

            fileUtils.createDirIfNotExist(this.tmpPath);
            fileUtils.saveFile(reqSequencesFilePath, reqPathSequences);

            MGFSMOutput output = mgfsmConnector.MGFSMExecute(input);

            List<PageFlow> rawPageFlows = new ArrayList<PageFlow>();

            for (MGFSMOutputItem outputItem : output.getOutputItems()) {

                PageFlow pageFlow = new PageFlow();

                pageFlow.setWeight(outputItem.getFrequency());
                pageFlow.setSequence(outputItem.getSequenceString().trim());
                pageFlow.setPageFlowSteps(new ArrayList<PageFlowStep>());

                for (String seqItem : outputItem.getSequence()) {
                    PageFlowStep pageFlowStep = new PageFlowStep();
                    pageFlowStep.setRequestPath(seqItem.split("&")[0]);
                    pageFlowStep.setHttpMethod(seqItem.split("&")[1]);
                    pageFlow.getPageFlowSteps().add(pageFlowStep);
                }
                rawPageFlows.add(pageFlow);
            }

            rawPageFlows.sort(new PageFlowWeightComparator());

            int i = 1;
            for (PageFlow pageFlow : rawPageFlows) {
                pageFlow.setId(i);
                i++;
            }

            pageFlows.setPageFlows(rawPageFlows);

        } catch (IOException | ProcessingException e) {
            e.printStackTrace();
        }

        return pageFlows;
    }

    /**
     * Generate String from Bundles object
     *
     * @param bundles User session pages created from access logs
     * @return Sequences of user paths from bundles
     */
    private String generateReqPathSequences(final Bundles bundles) {

        StringBuilder sb = new StringBuilder("");

        for (Map.Entry<String, Bundle> entry : bundles.getBundles().entrySet()) {
            Bundle bundle = entry.getValue();

            sb.append(entry.getKey());

            for (BundleItem bundleItem : bundle.getBundleItems()) {

                if (skipRestRequests && bundleItem.isRestRequest()) {
                    continue;
                }

                if (bundleItem.getControllerReqPath() != null) {
                    sb.append(" ");
                    sb.append(bundleItem.getControllerReqPath());
                    sb.append("&");
                    sb.append(bundleItem.getHttpMethod());
                }
            }

            sb.append('\n');
        }

        return sb.deleteCharAt(sb.length()-1).toString();
    }

    /**
     * Load MG-FSM configuration from file
     *
     * @return MGFSMInput configuration object
     */
    private MGFSMInput getMGFSMInput() throws IOException, ProcessingException {

        JsonReader reader = new JsonReader(new FileReader(this.mgfsmConfigFilePath));
        return GSON.fromJson(reader, MGFSMInput.class);
    }
}
