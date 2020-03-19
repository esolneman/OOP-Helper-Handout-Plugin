package controller;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowType;
import de.ur.mi.pluginhelper.logger.LogDataType;
import eventHandling.HandoutWebViewLinkListener;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import provider.LocalStorageDataProvider;
import toolWindow.HandoutContentScreen;

import java.io.*;

import static environment.LoggingMessageConstants.LINK_TO_HANDOUT;

public class WebViewController {
    private WebView webView;
    private String urlString;
    private static String MARK_ELEMENT = "mark";
    private static String CODE_ELEMENT = "code";
    private static String HASH_STRING = "#";

    public WebViewController() {
    }

    public WebView createHandoutWebView(String urlString) {
        this.urlString = urlString;
        webView = new WebView();
        HandoutWebViewLinkListener webViewLinkListener = new HandoutWebViewLinkListener(webView, urlString);
        webViewLinkListener.createListener();
        return webView;
    }

    public WebView createWebView(String urlString) {
        this.urlString = urlString;
        webView = new WebView();
        return webView;
    }

    //https://stackoverflow.com/questions/49070734/javafx-webview-link-to-anchor-in-document-doesnt-work-using-loadcontent
    public void goToLinkInHandout(String link, ToolWindow handoutToolWindow, HandoutContentScreen handoutContentScreen) {
        String newLocation = urlString + HASH_STRING + link;
        Platform.setImplicitExit(false);
        String finalLink = link;
        //https://jsoup.org/cookbook/input/parse-document-from-string
        org.jsoup.nodes.Document document = null;
        try {
            document = Jsoup.parse(LocalStorageDataProvider.getHandoutFileDirectory(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document.getElementById(link) != null) {
            LoggingController.getInstance().saveDataInLogger(LogDataType.HANDOUT, LINK_TO_HANDOUT, link);
            handleLink(handoutToolWindow,handoutContentScreen,finalLink, newLocation);
        }
    }

    private void handleLink(ToolWindow handoutToolWindow, HandoutContentScreen handoutContentScreen, String finalLink, String newLocation) {
        Platform.runLater(() -> {
            ApplicationManager.getApplication().invokeLater(() -> {
                openHandout(handoutToolWindow, handoutContentScreen);
            });
            if (finalLink.contains("/")) {
                navigateToPosition(finalLink, newLocation);
            } else {
                webView.getEngine().load(newLocation);
            }
        });
    }

    //scroll to position
    private void navigateToPosition(String finalLink, String newLocation) {
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
        if (handoutContentDocument != null) {
            addMarkElement(handoutContentDocument, finalLink);
        }
        saveChangedHandoutFile(handoutContentDocument, newLocation);
    }

    private void saveChangedHandoutFile(Document handoutContentDocument, String newLocation) {
        //https://stackoverflow.com/a/1001568
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(LocalStorageDataProvider.getHandoutFileDirectory()), "UTF-8"));
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
    }

    //if toolWindow is open, open 'handout' tab
    //if toolWindow is closed open toolwindow on handout tab in a small, floating variant
    private void openHandout(ToolWindow handoutToolWindow, HandoutContentScreen handoutContentScreen) {
        if (handoutToolWindow.isVisible()) {
            //open HandoutToolWindow and select the correct tab
            handoutToolWindow.getContentManager().setSelectedContent(handoutToolWindow.getContentManager().getContent(handoutContentScreen.getContent()));
        } else {
            handoutToolWindow.activate(() -> {
                handoutToolWindow.show(null);
                //https://intellij-support.jetbrains.com/hc/en-us/community/posts/206383859-How-to-float-tool-window-programatically-
                handoutToolWindow.setType(ToolWindowType.FLOATING, null);
            });
        }
    }

    //mark function name the link from code points to
    private void addMarkElement(org.jsoup.nodes.Document document, String elementName) {
        //https://jsoup.org/cookbook/modifying-data/set-html
        org.jsoup.nodes.Element ele = document.getElementById(elementName);
        org.jsoup.nodes.Element mark = document.createElement(MARK_ELEMENT);
        if (ele != null) {
            ele.child(0).wrap(mark.toString());
        }
    }

    //remove existing mark elements in the document
    private void deleteExistingMarkTag(org.jsoup.nodes.Document document) {
        Elements markElements = document.getElementsByTag(MARK_ELEMENT);
        for (int i = 0; i < markElements.size(); i++) {
            Element link = markElements.get(i);
            Element codeElement = document.createElement(CODE_ELEMENT);
            codeElement.text(link.child(0).text());
            markElements.parents().get(0).html(codeElement.toString());
        }
    }

    public void updateWebViewContent() {
        if (webView != null) {
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
            webView.getEngine().load(urlString);
        });
    }

}
