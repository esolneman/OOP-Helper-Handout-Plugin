package toolWindow;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.openapi.wm.ToolWindow;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import org.apache.commons.httpclient.URI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import provider.RepoLocalStorageDataProvider;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

//import jdk.tools.jlink.internal.Platform;
//import javafx.embed.swing.JFXPanel;

public class HandoutContentScreen {
    private JFXPanel handoutContent;
    private WebView webView;
    private ToolWindow handoutToolWindow;
    public HandoutContentScreen(ToolWindow toolWindow){
        handoutToolWindow = toolWindow;
        initToolWindowMenu();
        createContent();
        //setOnLinkListener();
    }

    private void initToolWindowMenu() {
        //ActionManager.createActionToolbar();
        //com.intellij.openapi.actionSystem.ActionManager.createActionToolbar ` and add AnAction on it via ActionGroup.
        //ToolWindowActionMAnager
        /*ActionGroup handoutActionGroup = new ActionGroup() {
            @NotNull
            @Override
            public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
                return new AnAction[0];
            }
        };
        ActionManager actionManager = ActionManager.getInstance();
        actionManager.createButtonToolbar("help", handoutActionGroup );*/
        /*ActionToolbar actionToolbar = (ActionToolbar) this.handoutContent;
        actionToolbar.setOrientation(SwingConstants.HORIZONTAL);
        //handoutContent.remove();
        handoutContent.add(actionToolbar.getComponent(), BorderLayout.PAGE_START);*/
    }

    private void createContent() {
        handoutContent = new JFXPanel();
/*      //JPanel panel = new JPanel();

        // JButton mit Text "Drück mich" wird erstellt
        JButton button = new JButton("Drück mich");

        // JButton wird dem Panel hinzugefügt
        handoutContent.add(button);*/

        File content = RepoLocalStorageDataProvider.getHandoutHtmlFormat();
        String htmlString = RepoLocalStorageDataProvider.getHandoutHtmlString();
        final String url = "file:///" + htmlString;

        //URL url = getClass().getResource("C:/Masterarbeit/TestProjekt/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/index.html");
        //String url = WebView.class.getResource(file).toExternalForm();
        Platform.runLater(() -> {
            try {
                final String urlString = content.toURI().toURL().toString();
                webView = new WebView();
                webView.getEngine().load(urlString);
                //webView.getEngine().load(url.toExternalForm());
                //URL url = getClass().getResource("index.html");
                //webEngine.load(url.toExternalForm());
                webView.getEngine().setJavaScriptEnabled(true);
                handoutContent.setScene(new Scene(webView));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        });
    }

    //https://stackoverflow.com/questions/15555510/javafx-stop-opening-url-in-webview-open-in-browser-instead
    //https://stackoverflow.com/questions/31909455/open-hyperlinks-in-javafx-webview-with-default-browser
    public void setOnLinkListener() {
        webView.getEngine().locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, final String oldValue, String newValue) {
                Desktop d = Desktop.getDesktop();
                String toBeopen = webView.getEngine().getLoadWorker().getMessage().trim();
                System.out.println("tobeopen: " + toBeopen);
                if (toBeopen.contains("http://") || toBeopen.contains("https://")) {
                    try {
                        Platform.runLater(() -> {
                            webView = new WebView();
                            String htmlString = RepoLocalStorageDataProvider.getHandoutHtmlString();
                            final String url = "file:///" + htmlString;
                            webView.getEngine().load(url);
                            handoutContent.setScene(new Scene(webView));
                        });
                        d.browse(new URL(toBeopen).toURI());
                        //BrowserUtil.browse(new URL(toBeopen).toURI());
                    }
                    catch (IOException | URISyntaxException e) {
                        System.out.println(e);
                    }
                }
            }
        });
    }


    public JFXPanel getContent() {
        System.out.println("Getting Content for Handout");
        /*if(webView != null) {
            System.out.println("webview not null");
            System.out.println(webView.getEngine().getDocument().getDocumentURI());
            Platform.runLater(() -> {
                webView.getEngine().reload();
            });
        }*/
        return handoutContent;
    }

    public void updateContent(){
        System.out.println("updateContent");
        if(webView != null) {
            System.out.println("webview not null");
            System.out.println(webView.getEngine().getDocument().getDocumentURI());
/*            Platform.runLater(() -> {
                System.out.println("run later");
                String htmlString = RepoLocalStorageDataProvider.getHandoutHtmlString();
                final String url = "file:///" + htmlString;
                webView.getEngine().load(url);
            });*/
        }
    }

    public void linkListenter(){
        BrowserUtil.browse("https://stackoverflow.com/questions/ask");
        String url;
        //BrowserUtil.browse(url);

    }
}
