package com.shpakovskiy.webcrawler.pageprocessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class SourceLoader {

    public String getPageSource(String pageAddress) {

        URL url;
        try {
            url = new URL(pageAddress);
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
            return null;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            StringBuilder pageContentBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                pageContentBuilder.append(line);
                pageContentBuilder.append(System.lineSeparator());
            }

            return pageContentBuilder.toString();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return null;
    }
}
