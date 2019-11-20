package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import cucumber.api.java.en_scouse.An;
import gui.HandoutPluginFXPanel;
import gui.NotesFXPanel;
import javafx.application.Platform;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import objects.Notes;
import provider.LocalStorageDataProvider;
import toolWindow.actions.AddNotesAction;
import webView.WebViewController;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;

public class NotesScreen extends SimpleToolWindowPanel {
    private NotesFXPanel notesContent;
    private ToolWindow noteToolWindow;
    private SimpleToolWindowPanel toolWindowPanel;
    private File notesFile;
    private Notes notes;
    private JList notesList;
    private JPanel noteContentPane;
    private JScrollPane notesScrollPane;
    private JTextArea textArea1;
    private HTMLEditor htmlEditor;
    private static File initNotesFile;
    private WebViewController webViewController;
    private String notesHtmlString;
    private File htmlFile;
    private static WebView webView;

    public NotesScreen(ToolWindow toolWindow) {
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        noteToolWindow = toolWindow;
        webViewController = new WebViewController();

        //TODO verschiebe to LOCALUSERSTORAGE
        initNotesFile = LocalStorageDataProvider.getNotesFile();
        //notesFile = LocalStorageDataProvider.getNotesFile();
        try {
            notesHtmlString = initNotesFile.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        noteContentPane = new JPanel();

        //TODO DISPLAY SOMETHING WHEN FILE IS EMPTY
/*       //TODO Write in Handler
        // compare with notesController
        //https://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
        Gson gson = new Gson();
        try (Reader reader = new FileReader(notesFile.getPath())) {
            // Convert JSON File to Java Object
            notes = gson.fromJson(reader, Notes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        createContent();
        initToolWindowMenu();
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(notesContent);
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup notesActionGroup = new DefaultActionGroup();
        AddNotesAction notesAction = (AddNotesAction) ActionManager.getInstance().getAction("Notes.AddEntry");
        notesAction.setNotesScreen(this);
        notesActionGroup.add(notesAction);
        final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("NotesTool", notesActionGroup, true);
        return actionToolbar.getComponent();
    }

    private void createContent() {
        notesContent = new NotesFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(notesHtmlString);
            notesContent.showHTMLEditor(notesHtmlString, webView);
        });
    }

    //@Override
    public void updateContent() { }

    public JPanel getContent() {
        return toolWindowPanel;
    }

    public void reloadWebView() {
        webViewController.updateWebViewContent();
    }
}
