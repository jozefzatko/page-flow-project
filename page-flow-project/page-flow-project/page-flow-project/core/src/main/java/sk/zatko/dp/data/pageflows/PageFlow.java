package sk.zatko.dp.data.pageflows;

import java.util.List;

import lombok.Data;

@Data
public class PageFlow {

    private int id;
    private String sequence;
    private double weight;
    private double weightOfSubsequences;
    private List<PageFlowStep> pageFlowSteps;
}
