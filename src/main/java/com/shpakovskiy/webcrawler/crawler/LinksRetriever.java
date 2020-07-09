package com.shpakovskiy.webcrawler.crawler;

import com.shpakovskiy.webcrawler.pageprocessor.SourceLoader;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinksRetriever implements Runnable {

    private int retrievingLimit;
    private AtomicInteger pagesCounter;
    private String pageAddress;
    private final SourceLoader sourceLoader = new SourceLoader();
    private List<LinkedHashSet<String>> listOfLinkSets;
    private int activeListLevel;

    private static final String HREF_INCLUDING_RELATIVE_LINK_REGEX =
            "href\\s*=\\s*\\\"(https?|www|\\/)[^\\/](?:(?!href=\\\"(https?|www|\\/)|\\\")[\\s\\S])*\\\"";

    private static final String HREF_EXCLUDING_RELATIVE_LINK_REGEX =
            "href\\s*=\\s*\"(https?|www)(?:(?!href=\"(https?|www)|\")[\\s\\S])*\"";

    private static final String INAPPROPRIATE_LINK_ENDING_REGEX =
            ".*(\\.mp3|\\.wav|\\.png|\\.jpg|\\.jpeg|\\.css|\\.js|\\.csv|\\.xml|\\.svg|\\.rss|\\.woff|\\.otf|\\.rdf|\\.gz|\\.apk|\\.exe|\\.asc|\\.pdf)";

    @Override
    public void run() {
        if (pagesCounter.get() - retrievingLimit != 0) {
            addAllLinks(sourceLoader.getPageSource(pageAddress));
        }
    }

    public void addAllLinks(String pageSource) {
        if (activeListLevel + 1 >= listOfLinkSets.size()) {
            listOfLinkSets.add(activeListLevel + 1, new LinkedHashSet<>());
        }

        Pattern pattern = Pattern.compile(HREF_EXCLUDING_RELATIVE_LINK_REGEX);

        if (pageSource != null) {
            Matcher matcher = pattern.matcher(pageSource);

            int lookFromPosition = 0;
            while (matcher.find(lookFromPosition) && pagesCounter.get() < retrievingLimit) {
                String dirtyLink = pageSource.substring(matcher.start(), matcher.end());
                String link = dirtyLink.substring(dirtyLink.indexOf('"') + 1, dirtyLink.lastIndexOf('"'));
                if (!link.matches(INAPPROPRIATE_LINK_ENDING_REGEX)
                        && isLinkUnique(listOfLinkSets, link)
                        && listOfLinkSets.get(activeListLevel + 1).add(link)) {

                    pagesCounter.incrementAndGet();
                }

                lookFromPosition = matcher.end() + 1;
            }
        }
    }

    private boolean isLinkUnique(List<LinkedHashSet<String>> listOfLinkSets, String link) {
        for (Set<String> set : listOfLinkSets) {
            if (set.contains(link)) {
                return false;
            }
        }
        return true;
    }

    public static class Builder {
        private final LinksRetriever newLinksRetriever;

        public Builder() {
            newLinksRetriever = new LinksRetriever();
        }

        public Builder retrievingLimit(int retrievingLimit) {
            newLinksRetriever.retrievingLimit = retrievingLimit;
            return this;
        }

        public Builder pagesCounter(AtomicInteger pagesCounter) {
            newLinksRetriever.pagesCounter = pagesCounter;
            return this;
        }

        public Builder pageAddress(String pageAddress) {
            newLinksRetriever.pageAddress = pageAddress;
            return this;
        }

        public Builder linksHolder(List<LinkedHashSet<String>> listOfLinkSets) {
            newLinksRetriever.listOfLinkSets = listOfLinkSets;
            return this;
        }

        public Builder activeListLevel(int activeListLevel) {
            newLinksRetriever.activeListLevel = activeListLevel;
            return this;
        }

        public LinksRetriever build() {
            return newLinksRetriever;
        }
    }
}
