package toolWindow;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.wm.ToolWindow;
import gui.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import objects.Notes;
import provider.LocalStorageDataProvider;
import webView.WebViewController;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;

public class NotesScreen {
    private HandoutPluginFXPanel notesContent;
    private ToolWindow noteToolWindow;
   // private SimpleToolWindowPanel toolWindowPanel;
    private File notesFile;
    private Notes notes;
    private JList notesList;
    private JPanel noteContentPane;
    private JScrollPane notesScrollPane;
    private JTextArea textArea1;
    private HTMLEditor htmlEditor;
    private static File content;
    private WebViewController webViewController;

    private String urlString;
    private static WebView webView;

    public NotesScreen(ToolWindow toolWindow) {
        //super(true, true);
        //toolWindowPanel = new SimpleToolWindowPanel(true);
        noteToolWindow = toolWindow;
        webViewController = new WebViewController();

        notesFile = LocalStorageDataProvider.getNotesFile();
        noteContentPane = new JPanel();

        //TODO Write in Handler
        //https://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
        Gson gson = new Gson();
        try (Reader reader = new FileReader(notesFile.getPath())) {
            // Convert JSON File to Java Object
            notes = gson.fromJson(reader, Notes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        content = LocalStorageDataProvider.getNotesFile();
        System.out.println("content HandoutContentScreen: " + content);

        try {
            urlString = content.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        createContent();
        initToolWindowMenu();
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        //toolWindowPanel.setToolbar(createToolbarPanel());
       // toolWindowPanel.setContent(noteContentPane);
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup notesActionGroup = new DefaultActionGroup();
        notesActionGroup.add(ActionManager.getInstance().getAction("Handout.Download"));
        final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("NotesTool", notesActionGroup, true);
        return actionToolbar.getComponent();
    }

    private void createContent() {
        notesContent = new HandoutPluginFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(urlString);
            notesContent.showHTMLEditor(urlString, webView);
        });
    }

    //@Override
    public void updateContent() { }

    public HandoutPluginFXPanel getContent() {
        return notesContent;
    }

}
