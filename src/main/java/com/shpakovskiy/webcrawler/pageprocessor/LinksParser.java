package com.shpakovskiy.webcrawler.pageprocessor;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinksParser implements LinksParserInterface {

    private static final String HREF_INCLUDING_RELATIVE_LINK_REGEX =
            "href\\s*=\\s*\\\"(https?|www|\\/)[^\\/](?:(?!href=\\\"(https?|www|\\/)|\\\")[\\s\\S])*\\\"";

    private static final String HREF_EXCLUDING_RELATIVE_LINK_REGEX =
            "href\\s*=\\s*\\\"(https?|www)(?:(?!href=\\\"(https?|www)|\\\")[\\s\\S])*\\\"";

    private static final String INAPPROPRIATE_LINK_ENDING_REGEX =
            ".*(\\.mp3|\\.wav|\\.png|\\.jpg|\\.jpeg|\\.css|\\.js|\\.csv|\\.xml|\\.svg|\\.rss|\\.woff|\\.otf)";

//    @Override
//    public HashSet<String> getAllLinks(String pageSource) {
//        HashSet<String> allLinks = new HashSet<>();
//
//        Pattern pattern = Pattern.compile(HREF_EXCLUDING_RELATIVE_LINK_REGEX);
//        Matcher matcher = pattern.matcher(pageSource);
//
//        int lookFromPosition = 0;
//        while (matcher.find(lookFromPosition)) {
//            String dirtyLink = pageSource.substring(matcher.start(), matcher.end());
//            String link = dirtyLink.substring(dirtyLink.indexOf('"') + 1, dirtyLink.lastIndexOf('"'));
//
//            if (!link.matches(INAPPROPRIATE_LINK_ENDING_REGEX)) {
//                allLinks.add(link);
//            }
//            lookFromPosition = matcher.end() + 1;
//        }
//
//        return allLinks;
//    }
//
//    @Override
//    public int addAllLinks(String pageSource, LinkedHashSet<String> destinationSet) {
//        if (destinationSet == null) {
//            destinationSet = new LinkedHashSet<>();
//        }
//
//        Pattern pattern = Pattern.compile(HREF_EXCLUDING_RELATIVE_LINK_REGEX);
//        Matcher matcher = pattern.matcher(pageSource);
//
//        int lookFromPosition = 0;
//        int addedLinks = 0;
//        while (matcher.find(lookFromPosition)) {
//            String dirtyLink = pageSource.substring(matcher.start(), matcher.end());
//            String link = dirtyLink.substring(dirtyLink.indexOf('"') + 1, dirtyLink.lastIndexOf('"'));
//            if (!link.matches(INAPPROPRIATE_LINK_ENDING_REGEX) && destinationSet.add(link)) {
//                addedLinks++;
//            }
//            lookFromPosition = matcher.end() + 1;
//        }
//        return addedLinks;
//    }

    @Override
    public int addAllLinks(String pageSource, LinkedHashSet<String> destinationSet, final int parsingLimit) {
        if (destinationSet == null) {
            destinationSet = new LinkedHashSet<>();
        }

        Pattern pattern = Pattern.compile(HREF_EXCLUDING_RELATIVE_LINK_REGEX); //TODO: Try to rewrite without using regex. Such approach can be faster.
        if (pageSource != null) {
            Matcher matcher = pattern.matcher(pageSource);

            int lookFromPosition = 0;
            int parsedLinksNumber = 0;
            while (matcher.find(lookFromPosition) && parsedLinksNumber < parsingLimit) {
                String dirtyLink = pageSource.substring(matcher.start(), matcher.end());
                String link = dirtyLink.substring(dirtyLink.indexOf('"') + 1, dirtyLink.lastIndexOf('"'));
                if (!link.matches(INAPPROPRIATE_LINK_ENDING_REGEX) && destinationSet.add(link)) {
                    parsedLinksNumber++;
                }
                lookFromPosition = matcher.end() + 1;
            }
            return parsedLinksNumber;
        }
        return 0;
    }
}
