package com.shpakovskiy.webcrawler.statistics;

import java.util.List;

public interface Table {

    void addItem(StatItem statItem);
    List<StatItem> getAsList();
    String toCsv();
}
