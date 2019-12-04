package eventHandling;

import com.intellij.openapi.project.Project;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.codefx.libfx.control.webview.WebViewHyperlinkListener;
import org.codefx.libfx.control.webview.WebViews;
import provider.RepoLocalStorageDataProvider;

import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//TODO Constants
public class HelpWebViewLinkListener {

    private WebView webView;
    String urlString;

    public HelpWebViewLinkListener(WebView webView, String urlString) {
        this.webView = webView;
        this.urlString = urlString;
    }

    public void createListener() {
        //https://github.com/CodeFX-org/LibFX/wiki/WebViewHyperlinkListener
        WebViewHyperlinkListener eventPrintingListener = event -> {
            //TODO: Refactor variable name
            String hyperlink = event.getURL().toString();
            System.out.println("HELP WebView: Listener: "+ hyperlink);
            if (hyperlink.contains("http://") || hyperlink.contains("https://") || hyperlink.contains("mailto")) {
                System.out.println("WebView: Link to externalPAge");
                handleLinkToExternalWebpage(hyperlink);
            }
            return false;
        };
        //TODO METHOD FOR THIS IN WEBVIEW CONTROOLER
        WebViews.addHyperlinkListener(
                webView, eventPrintingListener,
                HyperlinkEvent.EventType.ACTIVATED);
    }


    private void handleLinkToExternalWebpage(String toBeopen) {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI address = new URI(toBeopen);
                System.out.println("open external link: " + toBeopen);
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
