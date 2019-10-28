package webView;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindow;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.apache.commons.lang.WordUtils;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import toolWindow.HandoutContentScreen;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
                org.w3c.dom.Document documentJava = webView.getEngine().getDocument();
                Element ele = documentJava.getElementById(finalHeading);
                System.out.println("ele: " + ele.getNodeName());
                Element mark = documentJava.createElement("mark");
                System.out.println("mrk: " + mark);
                Element parentElement = (Element) ele.getParentNode();
                System.out.println("ele getParentNode : " + parentElement.getNodeName());
                parentElement.insertBefore(mark, ele);
                mark.appendChild(ele);
                System.out.println("ele getParentNode -new: " + ele.getParentNode());
                webView.getEngine().load(newLocation);

                System.out.println("second start: ");

                CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
                    System.out.println("second finish: ");
                    Platform.setImplicitExit(false);
                    Platform.runLater(() -> {
                        webView.getEngine().reload();
                    });
                });

                //https://stackoverflow.com/questions/29087077/is-it-possible-to-convert-html-into-xhtml-with-jsoup-1-8-1

            }else {
                webView.getEngine().load(newLocation);
            }

        });
    }
}
