package com.shpakovskiy.webcrawler.pageprocessor;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public interface LinksParserInterface {
//
//    HashSet<String> getAllLinks(String pageSource);
//    int addAllLinks(String pageSource, LinkedHashSet<String> destinationSet);
    int addAllLinks(String pageSource, LinkedHashSet<String> destinationSet, int parsingLimit);
}
