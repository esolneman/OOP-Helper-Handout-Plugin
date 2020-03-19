package eventHandling;

import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.codefx.libfx.control.webview.WebViewHyperlinkListener;
import org.codefx.libfx.control.webview.WebViews;

import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HelpWebViewLinkListener {

    private WebView webView;
    String urlString;
    private static String HTTP_PREFIX = "http://";
    private static String HTTPS_PREFIX = "https://";
    private static String MAIL_PREFIX = "mailto";

    public HelpWebViewLinkListener(WebView webView, String urlString) {
        this.webView = webView;
        this.urlString = urlString;
    }

    public void createListener() {
        WebViewHyperlinkListener eventPrintingListener = event -> {
            String hyperlink = event.getURL().toString();
            if(hyperlink != null){
                if (hyperlink.contains(HTTP_PREFIX) || hyperlink.contains(HTTPS_PREFIX) || hyperlink.contains(MAIL_PREFIX)) {
                    handleLinkToExternalWebpage(hyperlink);
                }
            }
            return false;
        };
        WebViews.addHyperlinkListener(webView, eventPrintingListener, HyperlinkEvent.EventType.ACTIVATED);
    }


    //open link in external browser and reload webview
    private void handleLinkToExternalWebpage(String link) {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI address = new URI(link);
                Platform.setImplicitExit(false);
                Platform.runLater(() -> {
                    webView.getEngine().load(urlString);
                });
                desktop.browse(address);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

}
