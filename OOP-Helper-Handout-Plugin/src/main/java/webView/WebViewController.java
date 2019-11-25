package webView;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowType;
import eventHandling.HandoutWebViewLinkListener;
import eventHandling.HelpWebViewLinkListener;
import gui.NoteAddingFrame;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.apache.commons.lang.WordUtils;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLInputElement;
import toolWindow.HandoutContentScreen;
import toolWindow.NotesScreen;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class WebViewController {
    private WebView webView;
    private HandoutWebViewLinkListener webViewLinkListener;
    private String urlString;

    public WebViewController(){}

    public WebView createHandoutWebView(String urlString){
        this.urlString = urlString;
        //Platform.setImplicitExit(false);
        //Platform.runLater(() -> {
            webView = new WebView();
            HandoutWebViewLinkListener webViewLinkListener = new HandoutWebViewLinkListener(webView, urlString);
            webViewLinkListener.createListener();
            return webView;
        //});
    }

    public WebView createWebView(String urlString){
        this.urlString = urlString;
        //Platform.setImplicitExit(false);
        //Platform.runLater(() -> {
        webView = new WebView();
        return webView;
        //});
    }

    public WebView createHelpWebView(String urlString){
        this.urlString = urlString;
        //Platform.setImplicitExit(false);
        //Platform.runLater(() -> {
        webView = new WebView();
        HelpWebViewLinkListener webViewLinkListener = new HelpWebViewLinkListener(webView, urlString);
        webViewLinkListener.createListener();
        return webView;
        //});
    }

    //https://stackoverflow.com/questions/49070734/javafx-webview-link-to-anchor-in-document-doesnt-work-using-loadcontent
    public void goToLocation(String heading, ToolWindow handoutToolWindow, HandoutContentScreen handoutContentScreen){
        System.out.println("heading: " + heading);
        //TODO: check or if (class ref)
        if(heading.contains(" ")){
            System.out.println("heading contains space: " + heading);
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
                        handoutToolWindow.show(null);
                        //https://intellij-support.jetbrains.com/hc/en-us/community/posts/206383859-How-to-float-tool-window-programatically-
                        handoutToolWindow.setType(ToolWindowType.FLOATING, null);
                        handoutToolWindow.getContentManager().setSelectedContent(handoutToolWindow.getContentManager().getContent(handoutContentScreen.getContent()));
                    });
                }
            });
            if (finalHeading.contains("/")){
                //https://stackoverflow.com/questions/52960101/how-to-edit-html-page-in-a-webview-from-javafx-without-reloading-the-page
                //https://stackoverflow.com/a/5882802
                org.w3c.dom.Document documentJava = webView.getEngine().getDocument();
                //TODO Sometimes Nullpointer
                Element ele = documentJava.getElementById(finalHeading);
                Element mark = documentJava.createElement("mark");
                //TODO Sometimes Nullpointer
                Element parentElement = (Element) ele.getParentNode();
                parentElement.insertBefore(mark, ele);
                mark.appendChild(ele);
                webView.getEngine().load(newLocation);
                //https://stackoverflow.com/a/53452586
                //TODO REFACTOR
                CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
                    updateWebViewContent();
                });

            }else {
                //TODO new method
                webView.getEngine().load(newLocation);
            }
        });
    }

    public void goToHeading(String heading){
        System.out.println("heading: " + heading);
        //TODO: check or if (class ref)
        if(heading.contains(" ")){
            System.out.println("heading contains space: " + heading);
            //https://stackoverflow.com/a/1892778
            heading = WordUtils.capitalize(heading);
            //https://stackoverflow.com/a/15633284
            heading = heading.replaceAll("\\s+","");
        }
        String newLocation = urlString + "#" + heading;
        System.out.println(newLocation);
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            //TODO new method
            webView.getEngine().load(newLocation);
        });

    }

    public void updateWebViewContent() {
        // TODO Error once
        // TODO test if load urlAtring is working
        if(webView != null){
            Platform.setImplicitExit(false);
            Platform.runLater(() -> {
                webView.getEngine().reload();
            });
        }
    }

    public void changeURL(String url) {
        // TODO Error once
        // TODO test if load urlAtring is working
        if(webView != null){
            Platform.setImplicitExit(false);
            Platform.runLater(() -> {
                webView.getEngine().load(url);
            });
        }
    }
}
