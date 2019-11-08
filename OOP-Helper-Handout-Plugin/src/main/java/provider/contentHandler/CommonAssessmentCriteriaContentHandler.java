package provider.contentHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import provider.LocalStorageDataProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CommonAssessmentCriteriaContentHandler implements ContentHandlerInterface{

    //TODO: Wait for HandoutContentDataProvider
    public static ArrayList<String> getNavHeadings() {
        ArrayList<String> headings = new ArrayList<>();
        File commonAssessmentFile = LocalStorageDataProvider.getCommonAssessmentCriteriaFileDirectory();
        if(commonAssessmentFile.getParentFile().exists()){
            Document handoutDocument;
            try {
                //https://stackoverflow.com/a/9611720
                handoutDocument = Jsoup.parse(commonAssessmentFile, null);
                //https://stackoverflow.com/questions/34392919/find-href-link-from-webpage-using-java
                //https://jsoup.org/apidocs/org/jsoup/select/Selector.html
                Elements navElement = handoutDocument.select("button");
                for (Element link : navElement) {
                    System.out.println("htmlFile CommonAssessmentCriteriaContentHandler: " + link.text());

                    //if(link.attr("href").contains("#")){
                        headings.add(link.text());
                    //}
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("htmlFile CommonAssessmentCriteriaContentHandler: " + headings.toString());

        return headings;
    }
}
