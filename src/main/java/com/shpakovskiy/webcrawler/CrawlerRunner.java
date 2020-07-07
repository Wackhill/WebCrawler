package com.shpakovskiy.webcrawler;

import com.shpakovskiy.webcrawler.crawler.WebCrawler;

import java.io.IOException;
import java.util.*;

public class CrawlerRunner {

    //https://www.cumhuriyet.com.tr/?hn=298710
    //https://community.diabetes.org/discuss

    public static void main(String[] args) {
        List<String> terms = new ArrayList<>();
        terms.add("Elon");
        terms.add("Musk");
        terms.add("Elon Musk");

        WebCrawler crawler = new WebCrawler("https://stackoverflow.com/questions/5080612/hashset-vs-linkedhashset#:~:text=LinkedHashSet%20is%20the%20ordered%20version,predictable%20in%20case%20of%20LinkedHashSet.", terms, 10000, 8);
        crawler.startCrawling();

//        LinkedHashSet<String> strings = Collections.synchronizedSet(new LinkedHashSet());
//        LinkedHashSet<String> strings = new LinkedHashSet<>();
//
//        strings.add("aaa");
//        strings.add("bbb");
//        strings.add("ccc");

/*
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        strings.add("vvv");
        strings.add("vvv");
        System.out.println(iterator.next());

 */
    }
}