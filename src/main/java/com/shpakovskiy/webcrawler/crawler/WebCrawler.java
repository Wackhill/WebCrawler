package com.shpakovskiy.webcrawler.crawler;

import com.shpakovskiy.webcrawler.pageprocessor.LinksParser;
import com.shpakovskiy.webcrawler.pageprocessor.SourceLoader;

import java.io.IOException;
import java.util.*;

public class WebCrawler implements Crawler {

    private final static int DEFAULT_MAX_PAGES_TO_VISIT = 10000;
    private final static int DEFAULT_MAX_DEPTH = 8;

    private String rootPageAddress;
    private List<String> terms;
    private int maxPagesToVisit;
    private int maxDepth;

    private int currentLevel = 0;
    private int uniquePages = 0;

    private List<LinkedHashSet<String>> linksList = new ArrayList<>();                              //The structure, which holds all links, discovered on all levels
    private Iterator currentPageIterator;

    private LinksParser linksParser = new LinksParser();

    public WebCrawler(String rootPageAddress, List<String> terms) {
        new WebCrawler(rootPageAddress, terms, DEFAULT_MAX_PAGES_TO_VISIT, DEFAULT_MAX_DEPTH);
    }

    public WebCrawler(String rootPageAddress, List<String> terms, int maxPagesToVisit, int maxDepth) {
        this.rootPageAddress = rootPageAddress;
        this.terms = terms;
        this.maxPagesToVisit = maxPagesToVisit;
        this.maxDepth = maxDepth;

        LinkedHashSet<String> rootPageSet = new LinkedHashSet<>();
        rootPageSet.add(rootPageAddress);
        linksList.add(currentLevel, rootPageSet);
        currentPageIterator = linksList.get(currentLevel).iterator();
    }

    public void startCrawling() {
        while (currentLevel < maxDepth && uniquePages < maxPagesToVisit) {
            while (currentPageIterator.hasNext() && currentLevel < maxDepth && uniquePages < maxPagesToVisit) {
                String currentPage = (String) currentPageIterator.next();
                System.out.println("Current page: " + currentPage);
                System.out.println("Counting some statistics");

                linksList.add(currentLevel + 1, new LinkedHashSet<>());
                uniquePages += linksParser
                        .addAllLinks(new SourceLoader().getPageSource(currentPage), linksList.get(currentLevel + 1), maxPagesToVisit - uniquePages);

                System.out.println("So, I've got: " + linksList.get(currentLevel + 1));
                System.out.println("Uni: " + uniquePages);
            }
            currentLevel++;
            currentPageIterator = linksList.get(currentLevel).iterator();

            System.out.println("========================= Lvl: " + currentLevel);
            System.out.println("========================= Uni1: " + uniquePages);
        }
    }


    private HashMap<String, List<Integer>> generateOccurrencesStatistics() {
        return null;
    }
}
