package com.shpakovskiy.webcrawler.crawler;

import com.shpakovskiy.webcrawler.statistics.OccurrencesTable;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class WebCrawler implements Crawler {

    //Default values
    private final static int DEFAULT_MAX_PAGES_TO_VISIT = 10000;
    private final static int DEFAULT_MAX_DEPTH = 8;

    //Number of retrieving threads
    private final int LINKS_RETRIEVING_THREADS_NUMBER = 7;
    private final int STAT_RETRIEVING_THREADS_NUMBER = 20;

    //Values, the crawler's work based on
    private List<String> terms;
    private int maxPagesToVisit;
    private int maxDepth;

    //Values, describing current crawler's state
    private int activeLevel = 0;
    private final AtomicInteger uniquePagesNumber = new AtomicInteger(1);

    //The structure, which holds all links, discovered on each level
    private final List<LinkedHashSet<String>> listOfLinkSets = new Vector<>();
    private final OccurrencesTable occurrencesTable = new OccurrencesTable();

    //Iterator, used to bypass every level of links
    private Iterator<String> currentLevelIterator;

    public WebCrawler(String rootPageAddress, List<String> terms) {
        new WebCrawler(DEFAULT_MAX_PAGES_TO_VISIT, DEFAULT_MAX_DEPTH, rootPageAddress, terms);
    }

    public WebCrawler(int maxPagesToVisit, int maxDepth, String rootPageAddress, List<String> terms) {
        prepareCrawler(rootPageAddress, terms);
        this.terms = terms;
        this.maxPagesToVisit = maxPagesToVisit;
        this.maxDepth = maxDepth;
    }

    public OccurrencesTable getOccurrencesStatistics() {
        retrieveLinks();
        retrieveOccurrencesStat();
        return occurrencesTable;
    }

    private void retrieveLinks() {
        while (activeLevel < maxDepth
                && uniquePagesNumber.get() < maxPagesToVisit
                && currentLevelIterator.hasNext()) {

            ExecutorService executorService = Executors.newFixedThreadPool(LINKS_RETRIEVING_THREADS_NUMBER);
            while (activeLevel < maxDepth
                    && uniquePagesNumber.get() < maxPagesToVisit
                    && currentLevelIterator.hasNext()) {

                executorService.submit(new LinksRetriever.Builder()
                        .pageAddress(currentLevelIterator.next())
                        .pagesCounter(uniquePagesNumber)
                        .retrievingLimit(maxPagesToVisit)
                        .linksHolder(listOfLinkSets)
                        .activeListLevel(activeLevel)
                        .build());
            }

            executorService.shutdown();
            try {
                executorService.awaitTermination(3, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            activeLevel++;
            currentLevelIterator = listOfLinkSets.get(activeLevel).iterator();
        }
    }

    private void retrieveOccurrencesStat() {
        List<String> allLinks = listOfLinkSets.stream().flatMap(LinkedHashSet::stream).collect(Collectors.toList());

        ExecutorService executorService = Executors.newFixedThreadPool(STAT_RETRIEVING_THREADS_NUMBER);
        for (String link : allLinks) {

            executorService.submit(new OccurrenceStatRetriever.Builder()
                    .linksHolder(terms)
                    .pageAddress(link)
                    .statTable(occurrencesTable)
                    .build());
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void prepareCrawler(String rootPageAddress, List<String> terms) {
        LinkedHashSet<String> rootPageSet = new LinkedHashSet<>();
        rootPageSet.add(rootPageAddress);
        listOfLinkSets.add(rootPageSet);
        currentLevelIterator = listOfLinkSets.get(activeLevel).iterator();

        terms.replaceAll(String::toUpperCase);
    }
}