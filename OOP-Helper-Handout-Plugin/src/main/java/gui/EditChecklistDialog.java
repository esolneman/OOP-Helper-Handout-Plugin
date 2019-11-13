package gui;

import com.google.gson.*;
import com.intellij.ui.treeStructure.Tree;
import gherkin.lexer.Pa;
import provider.LocalStorageDataProvider;
import provider.ParseChecklistJSON;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;

//ToDo create class for Logic
public class EditChecklistDialog {
    private JPanel editChecklistPanel;
    private String titleString = "Bearbeite die Checkliste";
    private String description;
    private JOptionPane checklistOptionPane;
    private JDialog editDialog;
    private JsonObject checklistJson;
    private File file;
    private TreeModel currentModel;
    private TreeModel newModel;
    private DefaultMutableTreeNode root;
    private JTree tree;

    public EditChecklistDialog() {
        editChecklistPanel = new JPanel();
        root = new DefaultMutableTreeNode();
        file = LocalStorageDataProvider.getChecklistUserData();
        description = ("Die vorgegebene Checkliste kannst du nicht anpassen. n/ Es ist möglich neue Aufgaben zu erstellen. n/ Vorhandene anzupassen oder zu löschen.");
        checklistOptionPane = new JOptionPane();
        getChecklistTreeModel();
        createPanel();
    }

    public void showPanel() {
        editDialog.setVisible(true);
        if (checklistOptionPane.getValue() != null){
            int value = (Integer) checklistOptionPane.getValue();
            if (value == JOptionPane.OK_OPTION) {
                //TODO Update Model
                newModel = tree.getModel();
                saveCreatedModelInFile(ParseChecklistJSON.getJsonFromTreeModel(newModel));
                System.out.println("Good.");
            } else if (value == JOptionPane.CANCEL_OPTION) {
                //TODO DIALOG BESTAETIGEN
                System.out.println("Try using the window decorations "
                        + "to close the non-auto-closing dialog. "
                        + "You can't!");
            }
        }
    }

    //https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
    private void addChangeListener() {
        checklistOptionPane.addPropertyChangeListener(
                e -> {
                    String prop = e.getPropertyName();

                    if (editDialog.isVisible()
                            && (e.getSource() == checklistOptionPane)
                            && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                        //If you were going to check something
                        //before closing the window, you'd do
                        //it here.
                        editDialog.setVisible(false);
                    }
                });
        editDialog.pack();
    }

    private void createPanel() {
        checklistOptionPane.setOptionType(2);
        //https://stackoverflow.com/a/21851201
        EditableChecklistTreeView cbt = new EditableChecklistTreeView();
        DefaultTreeModel model = new DefaultTreeModel(root);



        editChecklistPanel.setLayout(new FlowLayout());
        JFrame frame = new JFrame("Demo");
        tree = new Tree(root);
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        tree.putClientProperty("JTree.lineStyle", "Angled");
        JTextField textField = new JTextField();
        JButton addButton = new JButton();
        TreeCellEditor editor = new DefaultCellEditor(textField);
        tree.setEditable(true);
        tree.setCellEditor(editor);
        tree.setRowHeight(25);
        editChecklistPanel.add(tree);
        editChecklistPanel.setSize(600,450);
        frame.add(tree);
        frame.setVisible(true);

        cbt.setModel(model);
        editChecklistPanel.add(tree);
        checklistOptionPane.add(editChecklistPanel);

        editDialog = checklistOptionPane.createDialog(titleString);
        editDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
       // editDialog.add(editChecklistPanel);
        addChangeListener();
    }

    //TODO get TreeModel with Parser from LocalFile not data from prof.
    private void getChecklistTreeModel() {
        //TODO Create new Method
        //https://stackoverflow.com/a/34486879
        BufferedReader br = null;
        //get current tree model (only user nodes)
        try {
            br = new BufferedReader(new FileReader(file));
            JsonParser parser = new JsonParser();
            checklistJson = parser.parse(br).getAsJsonObject();
            currentModel = ParseChecklistJSON.getTreeModelFromJson(checklistJson);
            root = (DefaultMutableTreeNode) currentModel.getRoot();
        } catch (FileNotFoundException e) {
            // file doesn't exist - create file and new tree model
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException ex) {
                System.out.println("FILE NOT Created");
            }
            createNewModel();
            System.out.println("FILE NOT FOUND");
        }
    }

    private void createNewModel() {
        DefaultMutableTreeNode initNode = new DefaultMutableTreeNode("Eigene Checklist");
        DefaultTreeModel model = new DefaultTreeModel(initNode);
        JsonObject checklist = new JsonObject();
        JsonArray tasks = new JsonArray();
        checklist.add("checklist", tasks);
        saveCreatedModelInFile(checklist);
    }

    private void saveCreatedModelInFile(JsonObject checklist) {
        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(checklist, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
