package gui;

import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EditChecklistDialog {
    private JPanel editChecklistPanel;
    private String titleString = "Bearbeite die Checkliste";
    private String description;
    private JOptionPane editChecklistPane;
    private JDialog editDialog;

    public EditChecklistDialog() {
        editChecklistPanel = new JPanel();
        description = ("Die vorgegebene Checkliste kannst du nicht anpassen. n/ Es ist möglich neue Aufgaben zu erstellen. n/ Vorhandene anzupassen oder zu löschen.");
        editChecklistPane = new JOptionPane();
        getChecklistTreeModel();
        createPanel();
    }

    public void showPanel() {

        editDialog.setVisible(true);

        if (editChecklistPane.getValue() != null){
            int value = (Integer) editChecklistPane.getValue();
            if (value == JOptionPane.OK_OPTION) {
                //TODO Update Model
                System.out.println("Good.");
            } else if (value == JOptionPane.CANCEL_OPTION) {
                //TODO DIALOG BESTAETIGEN
                System.out.println("Try using the window decorations "
                        + "to close the non-auto-closing dialog. "
                        + "You can't!");
            }
        }
    }

    private void addChangeListener() {
        editChecklistPane.addPropertyChangeListener(
                e -> {
                    String prop = e.getPropertyName();

                    if (editDialog.isVisible()
                            && (e.getSource() == editChecklistPane)
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
        editChecklistPane.setOptionType(2);
        editDialog = editChecklistPane.createDialog(titleString);
        editDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addChangeListener();
    }

    private void getChecklistTreeModel() {
    }
}
