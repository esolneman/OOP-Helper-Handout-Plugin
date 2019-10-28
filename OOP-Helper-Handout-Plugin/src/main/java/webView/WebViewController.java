package webView;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindow;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import provider.LocalStorageDataProvider;
import sun.net.www.protocol.file.FileURLConnection;
import sun.net.www.protocol.http.HttpURLConnection;
import toolWindow.HandoutContentScreen;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

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
    public void goToLocation(String heading, ToolWindow handoutToolWindow, HandoutContentScreen handoutContentScreen){
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

            ApplicationManager.getApplication().invokeLater(() -> {
                System.out.println("ToolWindow isVisible: "+ handoutToolWindow.isVisible());
                if(handoutToolWindow.isVisible()) {
                    //open HandoutToolWindow and select the correct tab
                    handoutToolWindow.getContentManager().setSelectedContent(handoutToolWindow.getContentManager().getContent(handoutContentScreen.getContent()));
                }else{
                    //TODO open small part of tool window
                    handoutToolWindow.activate(() -> {
                        handoutToolWindow.getContentManager().setSelectedContent(handoutToolWindow.getContentManager().getContent(handoutContentScreen.getContent()));
                        //ToolWindowViewModeAction toolWindowViewModeAction
                        //ToolWindowViewModeAction.ViewMode.DockUnpinned;
                        //handoutToolWindow.;
                    });
                }
            });

            if (finalHeading.contains("/")){
                //https://stackoverflow.com/questions/52960101/how-to-edit-html-page-in-a-webview-from-javafx-without-reloading-the-page
                final Document document;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                //https://stackoverflow.com/questions/29087077/is-it-possible-to-convert-html-into-xhtml-with-jsoup-1-8-1
                File htmlFile = LocalStorageDataProvider.getHandoutFileDirectory();
                InputStream is = null;
                try {
                    document = Jsoup.parse(htmlFile, "UTF-8");
                    W3CDom w3CDom = new W3CDom();
                    org.w3c.dom.Document documentW3 = w3CDom.fromJsoup(document);
                    System.out.println(finalHeading);
                    System.out.println("getDoctype: " + documentW3.getDoctype());


                    Element ele = documentW3.getElementById(finalHeading);
                    System.out.println("ele: " + ele);

                    org.jsoup.nodes.Element jsoupEle = document.getElementById(finalHeading);
                    System.out.println("JsoueEle: " + jsoupEle.html());

                    Element eleEngine = webView.getEngine().getDocument().getElementById(finalHeading);
                    System.out.println("eleEngine: " + eleEngine.getTagName());


                    //System.out.println("ele: " + ele.getNodeName());
                    Element mark = documentW3.createElement("mark");
                    System.out.println("mrk: " + mark);
                    Element parentElement = (Element) ele.getParentNode();
                    System.out.println("ele getParentNode : " + parentElement.getNodeName());
                    parentElement.insertBefore(mark, ele);
                    mark.appendChild(ele);
                    System.out.println("ele getParentNode -new: " + ele.getParentNode());

                    String highlightOutput = LocalStorageDataProvider.testFile();

                    String highlightLocation = highlightOutput + "#" + finalHeading;
                    webView.getEngine().load(newLocation);

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    IOUtils.closeQuietly(is);

                }

                webView.getEngine().load(newLocation);

            }else {
                webView.getEngine().load(newLocation);
            }

        });
    }
}
