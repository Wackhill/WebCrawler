package com.shpakovskiy.webcrawler.statistics;

import java.util.ArrayList;
import java.util.List;

public class OccurrencesTable implements Table {

    private final List<StatItem> occurrencesTable = new ArrayList<>();

    @Override
    public void addItem(StatItem statItem) {
        occurrencesTable.add(statItem);
    }

    @Override
    public List<StatItem> getAsList() {
        return occurrencesTable;
    }

    @Override
    public String toCsv() {
        StringBuilder tableBuilder = new StringBuilder();

        for (StatItem statItem : occurrencesTable) {
            tableBuilder.append(statItem.toCsv()).append("\n");
        }

        return tableBuilder.toString();
    }
}
