package gui;

import com.google.gson.*;
import com.intellij.ui.treeStructure.Tree;
import provider.LocalStorageDataProvider;
import provider.ParseChecklistJSON;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;

//ToDo create class for Logic
//TODO crate class for this TREE
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
    private JButton deleteButton;
    private JButton addButton;
    private DefaultTreeModel model;

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
        model = new DefaultTreeModel(root);


        editChecklistPanel.setLayout(new FlowLayout());
        tree = new Tree(root);
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        tree.putClientProperty("JTree.lineStyle", "Angled");
        JTextField textField = new JTextField();
        addButton = new JButton();

        TreeCellEditor editor = new DefaultCellEditor(textField);
        tree.setEditable(true);
        tree.setCellEditor(editor);
        tree.setRowHeight(25);
        editChecklistPanel.add(tree);
        editChecklistPanel.setSize(600,450);


        deleteButton = new JButton();
        deleteButton.setText("Delete Node");
        cbt.setModel(model);
        editChecklistPanel.add(tree);
        addButton.setText("Add Node on Marked Node");
        checklistOptionPane.add(addButton);
        checklistOptionPane.add(deleteButton);
        checklistOptionPane.add(editChecklistPanel);
        editDialog = checklistOptionPane.createDialog(titleString);
        editDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
       // editDialog.add(editChecklistPanel);
        addChangeListener();
        addButtonListener();
    }

    private void addButtonListener() {
        addButton.addActionListener(actionEvent -> {
            System.out.println("ADD");
            //http://www.java2s.com/Tutorials/Java/Swing_How_to/JTree/Change_nodes_in_JTree.htm
            //https://www.rgagnon.com/javadetails/java-0211.html
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode == null) {
                System.out.println("NODE NULL");
                return;
            }
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("newChild");
            selectedNode.add(newNode);
            ((DefaultTreeModel )tree.getModel()).nodeStructureChanged(newNode.getParent());
            TreeNode[] nodes = model.getPathToRoot(newNode);
            TreePath path = new TreePath(nodes);
            tree.scrollPathToVisible(path);
        });

        //https://www.rgagnon.com/javadetails/java-0211.html
        deleteButton.addActionListener(actionEvent -> {
            System.out.println("DELETE");
            TreePath path = tree.getSelectionPath();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            DefaultMutableTreeNode parentnode = (DefaultMutableTreeNode) node.getParent();
            if (node == null) {
                System.out.println("NODE NULL");
                return;
            }
            parentnode = (DefaultMutableTreeNode)node.getParent();
            node.removeAllChildren();
            parentnode.remove(node);
            ((DefaultTreeModel )tree.getModel()).nodeStructureChanged((TreeNode)node);
        });
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
