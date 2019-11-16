package toolWindow;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBList;
import gui.HandoutPluginFXPanel;
import objects.Notes;
import provider.LocalStorageDataProvider;

import javax.swing.*;
import java.io.*;

public class NotesScreen extends SimpleToolWindowPanel implements PluginToolWindowTabsInterface {

    private HandoutPluginFXPanel noteContent;
    private ToolWindow noteToolWindow;
    private static File content;
    private SimpleToolWindowPanel toolWindowPanel;
    private File notesFile;
    private Notes notes;
    private JList notesList;
    private JPanel noteContentPane;
    private JScrollPane notesScrollPane;
    private JTextArea textArea1;

    public NotesScreen(ToolWindow toolWindow) {
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        noteToolWindow = toolWindow;
        notesFile = LocalStorageDataProvider.getNotesFile();

        //TODO Write in Handler
        //https://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
        Gson gson = new Gson();
        try (Reader reader = new FileReader(notesFile.getPath())) {
            // Convert JSON File to Java Object
            notes = gson.fromJson(reader, Notes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        createContent();
        initToolWindowMenu();
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(noteContent);
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup notesActionGroup = new DefaultActionGroup();
        final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("NotesTool", notesActionGroup, true);
        return actionToolbar.getComponent();
    }

    private void createContent() {
        noteContent = new HandoutPluginFXPanel();

        DefaultListModel dlm = new DefaultListModel();

        String[] content = {"Some", "Random", "Words"};

        for(String word : content)
        {
            dlm.addElement(word);
        }
        notesList = new JBList(dlm);
        textArea1.setText("dfghjkl");
        notesScrollPane.add(textArea1);
        notesScrollPane.add(notesList);
        //noteContentPane.add(notesScrollPane);
        noteContent.add(notesScrollPane);
    }

    @Override
    public void updateContent() {
    }

    public JComponent getToolbar(){
        return toolWindowPanel.getToolbar();
    }


    public JPanel getContent() {
        return toolWindowPanel;
    }

}
