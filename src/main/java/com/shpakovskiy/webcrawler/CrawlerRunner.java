package com.shpakovskiy.webcrawler;

import com.shpakovskiy.webcrawler.crawler.WebCrawler;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CrawlerRunner {

    //https://www.cumhuriyet.com.tr/?hn=298710
    //https://community.diabetes.org/discuss

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {

        long time1 = System.currentTimeMillis();
        List<String> terms = new ArrayList<>();

        terms.add("Gilbert Strang");
        terms.add("Math");
        terms.add("Python");
        terms.add("Operating Systems");
        terms.add("Trump");

        //https://de.linkedin.com/jobs/community-and-social-services-stellen/
        WebCrawler crawler = new WebCrawler(10000, 8, "https://ocw.mit.edu/index.htm", terms);
        System.out.println(crawler.getOccurrencesStatistics().toCsv());
        System.out.println("Whole time: " + (System.currentTimeMillis() - time1));
    }

}