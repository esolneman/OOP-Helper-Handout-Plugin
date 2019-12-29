package webView;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowType;
import com.sun.webkit.dom.HTMLAnchorElementImpl;
import com.sun.webkit.dom.HTMLBodyElementImpl;
import com.sun.webkit.dom.NodeListImpl;
import eventHandling.HandoutWebViewLinkListener;
import eventHandling.HelpWebViewLinkListener;
import gui.NoteAddingFrame;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.apache.commons.lang.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import provider.LocalStorageDataProvider;
import toolWindow.HandoutContentScreen;
import toolWindow.NotesScreen;

import java.io.*;
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
        //HelpWebViewLinkListener webViewLinkListener = new HelpWebViewLinkListener(webView, urlString);
        //webViewLinkListener.createListener();
        return webView;
        //});
    }

    //TODO remove first if and check if coditions possibilities
    //https://stackoverflow.com/questions/49070734/javafx-webview-link-to-anchor-in-document-doesnt-work-using-loadcontent
    public void goToLocation(String heading, ToolWindow handoutToolWindow, HandoutContentScreen handoutContentScreen) {
        System.out.println("goToLocation: " + heading);
        //TODO: check or if (class ref)
        if(heading.contains(" ")){
            System.out.println("goToLocation heading contains space: " + heading);
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
                    handoutToolWindow.activate(() -> {
                        handoutToolWindow.show(null);
                        //https://intellij-support.jetbrains.com/hc/en-us/community/posts/206383859-How-to-float-tool-window-programatically-
                        handoutToolWindow.setType(ToolWindowType.FLOATING, null);
                    });
                }
            });
            if (finalHeading.contains("/")){
                System.out.println("goToLocation finalHeading: " + finalHeading);
                System.out.println("goToLocation newLocation: " + newLocation);
                //TODO check source
                //https://stackoverflow.com/questions/52960101/how-to-edit-html-page-in-a-webview-from-javafx-without-reloading-the-page
                //https://stackoverflow.com/a/5882802


                webView.getEngine().load(newLocation);
                 //https://jsoup.org/cookbook/input/parse-document-from-string
                org.jsoup.nodes.Document handoutContentDocument = null;
                try {
                    handoutContentDocument = Jsoup.parse(LocalStorageDataProvider.getHandoutFileDirectory(), "UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                deleteExistingMarkTag(handoutContentDocument);

                if(handoutContentDocument != null){
                    //https://jsoup.org/cookbook/modifying-data/set-html
                    org.jsoup.nodes.Element ele = handoutContentDocument.getElementById(finalHeading);
                    org.jsoup.nodes.Element mark = handoutContentDocument.createElement("mark");
                    if(ele != null){
                        ele.child(0).wrap(mark.toString());
                    }
                }

                //https://stackoverflow.com/a/1001568
                Writer out = null;
                try {
                    out = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(LocalStorageDataProvider.getHandoutFileDirectory()), "UTF-8"));
                    System.out.println(handoutContentDocument.body().html());
                    out.write(String.valueOf(handoutContentDocument));

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        out.close();
                        webView.getEngine().load(newLocation);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }else {
                //TODO new method
                webView.getEngine().load(newLocation);
            }
        });
    }

    private void deleteExistingMarkTag(org.jsoup.nodes.Document document) {
        Elements markElements= document.getElementsByTag("mark");
        for (int i = 0; i < markElements.size(); i++) {
            Element link = markElements.get(i);
            Element codeElement =  document.createElement("code");
            codeElement.text(link.child(0).text());
            markElements.parents().get(0).html(codeElement.toString());
        }
    }

    public void updateWebViewContent() {
        // TODO Error once
        // TODO test if load urlstring is working
        System.out.println("updateWebViewContent");
        if(webView != null){
            System.out.println("updateWebViewContent not null");
            Platform.setImplicitExit(false);
            Platform.runLater(() -> {
            webView.getEngine().setJavaScriptEnabled(true);
            webView.getEngine().reload();
            });
        }
    }

    public void loadNewURL(String urlString) {
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            //TODO new method
            webView.getEngine().load(urlString);
        });
    }

}
