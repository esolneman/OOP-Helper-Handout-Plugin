package webView;

import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import provider.LocalStorageDataProvider;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class WebViewController {
    private WebView webView;
    private WebViewLinkListener webViewLinkListener;
    private String urlString;

    public WebViewController(){
    }

    public WebView createWebView(String urlString){
        this.urlString = urlString;
        //Platform.setImplicitExit(false);
        //Platform.runLater(() -> {
            webView = new WebView();
            WebViewLinkListener webViewLinkListener = new WebViewLinkListener(webView, urlString);
            return webView;
        //});
    }

    //https://stackoverflow.com/questions/49070734/javafx-webview-link-to-anchor-in-document-doesnt-work-using-loadcontent
    public void goToLocation(String heading){
        System.out.println("heading: " + heading);
        //TODO: check or if (class ref)
        if(heading.contains(" ")){
            //https://stackoverflow.com/a/1892778
            heading = WordUtils.capitalize(heading);
            //https://stackoverflow.com/a/15633284
            heading = heading.replaceAll("\\s+","");
        }
        String newLocation = urlString + "#" + heading;
        System.out.println(newLocation);
        Platform.setImplicitExit(false);
        String finalHeading = heading;
        Platform.runLater(() -> {
            /*

            //https://stackoverflow.com/questions/52960101/how-to-edit-html-page-in-a-webview-from-javafx-without-reloading-the-page

            final Document document;
            Element ele = webView.getEngine().getDocument().getElementById(finalHeading);
            System.out.println("ele: " + ele.getTagName());
            Element mark = webView.getEngine().getDocument().createElement("mark");
            System.out.println("mrk: " + mark.getTagName());
            Element parentElement = (Element) ele.getParentNode();
            System.out.println("ele getParentNode : " + parentElement.getNodeName());
            parentElement.insertBefore(mark, ele);
            mark.appendChild(ele);
            System.out.println("ele getParentNode -new: " + ele.getParentNode());

             DOMSource domSource = new DOMSource(webView.getEngine().getDocument());

             StreamResult result = new StreamResult(urlString);
             TransformerFactory tf = TransformerFactory.newInstance();
             Transformer transformer = null;

             try {
                 transformer = tf.newTransformer();
                 transformer.setOutputProperty(OutputKeys.METHOD, "html");
                 transformer.transform(domSource, result);
             } catch (TransformerConfigurationException e) {
                 e.printStackTrace();
             } catch (TransformerException e) {
                 e.printStackTrace();
             }



            try {
                //https://stackoverflow.com/questions/29087077/is-it-possible-to-convert-html-into-xhtml-with-jsoup-1-8-1

                document = Jsoup.parse(LocalStorageDataProvider.getHandoutFileDirectory(), "UTF-8");
                document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
                File outputFile = new File(LocalStorageDataProvider.getHandoutStringDirectory());
                FileUtils.writeStringToFile(outputFile, document.html(), "UTF-8");

            } catch (IOException e) {
                e.printStackTrace();
            }*/

            
            webView.getEngine().load(newLocation);

        });
    }
}
