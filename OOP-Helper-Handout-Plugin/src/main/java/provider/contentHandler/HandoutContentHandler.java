package provider.contentHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import provider.LocalStorageDataProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HandoutContentHandler implements ContentHandlerInterface{

    //TODO: Wait for HandoutContentDataProvider
    public static ArrayList<String> getNavHeadings() {
        ArrayList<String> headings = new ArrayList<>();
        File htmlFile = LocalStorageDataProvider.getHandoutFileDirectory();
        if(htmlFile.getParentFile().exists()){
            Document handoutDocument;
            try {
                //https://stackoverflow.com/a/9611720
                handoutDocument = Jsoup.parse(htmlFile, null);
                //https://stackoverflow.com/questions/34392919/find-href-link-from-webpage-using-java
                //https://jsoup.org/apidocs/org/jsoup/select/Selector.html
                Elements navElement = handoutDocument.select(" nav>ul>a[href]");
                for (Element link : navElement) {
                    if(link.attr("href").contains("#")){
                        headings.add(link.text());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("htmlFile ContentHandler: " + headings.toString());
        return headings;
    }
}
