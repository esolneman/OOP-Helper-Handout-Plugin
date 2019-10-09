package environment;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HandoutPluginFXPanel extends JFXPanel {

    public void showHandoutWebView(String urlString) {
        Platform.runLater(() -> {
            WebView webView = new WebView();
            webView.getEngine().load(urlString);
            webView.getEngine().setJavaScriptEnabled(true);
            this.setScene(new Scene(webView));
            setOnLinkListener(webView, urlString);
        });
    }

    //https://stackoverflow.com/questions/15555510/javafx-stop-opening-url-in-webview-open-in-browser-instead
    //https://stackoverflow.com/questions/31909455/open-hyperlinks-in-javafx-webview-with-default-browser
    private void setOnLinkListener(WebView webView, String urlString) {
        webView.getEngine().locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, final String oldValue, String newValue) {
                Desktop d = Desktop.getDesktop();
                System.out.println("message: " + webView.getEngine().getLoadWorker().getMessage());
                String toBeopen = webView.getEngine().getLoadWorker().getMessage().trim();
                System.out.println("tobeopen: " + toBeopen);
                try {
                    URI address = new URI(observable.getValue());
                    System.out.println("address: " + address);
                    if (toBeopen.contains("http://") || toBeopen.contains("https://") || toBeopen.contains("mailto")) {
                        Platform.setImplicitExit(false);
                        Platform.runLater(() -> {
                            webView.getEngine().load(urlString);
                        });
                        d.browse(address);
                    }
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showContent() {
        System.out.println("in: showContent");
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            System.out.println("in: showContent (RunLater-Part)");
            Label helloWord = new Label();
            helloWord.setText("Hello World");
            this.setScene(new Scene(helloWord, 50, 50));
        });
    }

    public void setWebView() {
    }

    //public void
    public void sayName() {
            System.out.println("I am a MyJFXPanel");
        }
}
