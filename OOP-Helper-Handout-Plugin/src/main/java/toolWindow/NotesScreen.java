package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import controller.LinkToHandoutController;
import controller.LoggingWebViewController;
import controller.NotesController;
import de.ur.mi.pluginhelper.logger.LogDataType;
import gui.NoteAddingFrame;
import gui.PluginWebViewFXPanel;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import webView.WebViewController;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;

public class NotesScreen extends SimpleToolWindowPanel {
    private PluginWebViewFXPanel notesContent;
    private ToolWindow noteToolWindow;
    private SimpleToolWindowPanel toolWindowPanel;
    private static File initNotesFile;
    private WebViewController webViewController;
    private String notesHtmlString;
    private static WebView webView;
    private NoteAddingFrame noteAddingFrame;
    private LoggingWebViewController loggingWebViewController;

    public NotesScreen(ToolWindow toolWindow) {
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        noteToolWindow = toolWindow;
        webViewController = new WebViewController();

        //TODO verschiebe to LOCALUSERSTORAGE
        initNotesFile = LocalStorageDataProvider.getNotesFile();
        try {
            notesHtmlString = initNotesFile.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //TODO DISPLAY SOMETHING WHEN FILE IS EMPTY
        createContent();
        initToolWindowMenu();
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setContent(notesContent);
    }

    private void createContent() {
        notesContent = new PluginWebViewFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(notesHtmlString);
            notesContent.showHandoutWebView(notesHtmlString, webView);
            webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    NotesController.getInstance().setWebView(webView);
                    initEditNotesButtonListener();
                }
            });
        });
    }

    //https://stackoverflow.com/a/34547416
    //create listener for "edit-notes" button in webView
    private void initEditNotesButtonListener() {
        noteAddingFrame = NoteAddingFrame.getInstance();
        noteAddingFrame.setNotesScreen(this);
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        window.setMember("noteAddingFrame", NoteAddingFrame.getInstance());
    }

    //@Override
    //TODO NOOOOOO
    // OVERRITE WHEN HANDOUT DATA IS UPDATED
    // PREVENT THIS FROM HAPPENING
    public void updateContent() {
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }

    //Todo
    // Had to be newly created, because when editing the html it is parsed to XHTML
    // and because of that, the button is not recogniced anymore
    public void reloadWebView() {
        Platform.runLater(() -> {
            webView = webViewController.createWebView(notesHtmlString);
            webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    loggingWebViewController = new LoggingWebViewController(webView, LogDataType.NOTES);
                    loggingWebViewController.addLoggingKeyEvents();
                    loggingWebViewController.addLoggingMouseEvents();
                }
            });
            notesContent.showHandoutWebView(notesHtmlString, webView);
            initEditNotesButtonListener();
        });
    }
}
