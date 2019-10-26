package provider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HandoutContentHandler {

    //TODO: Wait for HandoutContentDataProvider
    public static ArrayList<String> getNavHeadings() {
        ArrayList<String> headings = new ArrayList<>();
        File htmlFile = LocalStorageDataProvider.getHandoutFileDirectory();
        Document yourPage;
        try {
            //https://stackoverflow.com/a/9611720
            yourPage = Jsoup.parse(htmlFile, null);
            //https://stackoverflow.com/questions/34392919/find-href-link-from-webpage-using-java
            Elements aElement = yourPage.select("a[href]");
            for (Element link : aElement) {
                if(link.attr("href").contains("#")){
                    headings.add(link.text());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return headings;
    }
}
