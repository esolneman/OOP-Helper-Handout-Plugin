package gui;

import com.intellij.openapi.ui.DialogWrapper;
import javafx.application.Application;
import javafx.event.EventDispatcher;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


// J OPTION PANE
public class InformChangesPanel{
    private JButton okButton;
    private JTextPane title;
    private JList commitMessages;
    private JTextPane subtitle;
    private JPanel changesPanel;
    private String titleString;
    private String subtitleString;
    private ArrayList<String> commitMessagesList;
    private Integer numCommits;
    private JDialog jDialog;
    private JFrame frame;

    public InformChangesPanel(ArrayList<String> commitMessages) {
        commitMessagesList = commitMessages;
        numCommits = commitMessagesList.size();
        jDialog = new JDialog();
        titleString = "Eine Aktuelle Version der Content Daten wurde heruntergeladen";
        subtitleString = numCommits + "Änderungen wurden hinzugefügt:";
        createPanel();
    }


    public void showPanel() {
        //TODO replace woth dispatch thread
        frame.setVisible(true);
    }

    private void createPanel() {
        commitMessages = new JList();
        commitMessages.setListData(commitMessagesList.toArray());
        // create a jframe
        frame = new JFrame("JOptionPane showMessageDialog example");
        // show a joptionpane dialog using showMessageDialog
        JOptionPane.showMessageDialog(frame,
                subtitleString  + commitMessages,
                titleString, JOptionPane.INFORMATION_MESSAGE);
    }
}
