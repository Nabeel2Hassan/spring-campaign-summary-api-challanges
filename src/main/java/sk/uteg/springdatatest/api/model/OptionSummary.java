package sk.uteg.springdatatest.api.model;

import lombok.Data;

@Data
public class OptionSummary {
    private String text;
    private int occurrences;

    public OptionSummary(String text, int occurrences) {
        this.text = text;
        this.occurrences = occurrences;
    }
}
