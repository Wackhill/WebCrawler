package com.shpakovskiy.webcrawler.statistics;

import java.util.List;
import java.util.stream.Collectors;

public class StatItem {

    private String pageAddress;
    private List<Integer> occurrencesStat;

    public StatItem(String pageAddress, List<Integer> occurrencesStat) {
        this.pageAddress = pageAddress;
        this.occurrencesStat = occurrencesStat;
    }

    @Override
    public String toString() {
        return "StatItem{" +
                "pageAddress='" + pageAddress + '\'' +
                ", occurrencesStat=" + occurrencesStat +
                '}';
    }

    public String toCsv() {
        return pageAddress + "," + occurrencesStat.stream()
                                    .map(String::valueOf)
                                    .collect(Collectors.joining(","));
    }
}
