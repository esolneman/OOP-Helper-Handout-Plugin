package gui;

import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.io.UTFDataFormatException;
import java.util.ArrayList;


// J OPTION PANE
public class CommitChangesDialog {
    private JPanel changesPanel;
    private String titleString;
    private String subtitleString;
    private ArrayList<String> commitMessagesList;
    private Integer numCommits;
    private JDialog jDialog;
    private JFrame frame;
    public JDialog commitChangesDialog;
    public JPanel testPanel;

    //TODO REPLACE WITH CONSTANT MESSAGES
    public CommitChangesDialog(ArrayList<String> commitMessages) {
        commitMessagesList = commitMessages;
        numCommits = commitMessagesList.size();
        changesPanel = new JPanel();
        titleString = "Eine Aktuelle Version der Content Daten wurde heruntergeladen";
        subtitleString = numCommits + "Änderungen wurden hinzugefügt:";
        createPanel();
    }


    public void showPanel() {
        //TODO replace woth dispatch thread
        JOptionPane.showMessageDialog(null, changesPanel, titleString, JOptionPane.INFORMATION_MESSAGE);
    }

    private void createPanel() {
        //
       // commitMessages.setListData(commitMessagesList.toArray());
        //create a jframe
        //frame = new JFrame(titleString);


        JTextPane subTitle = new JTextPane();
        subTitle.setText(subtitleString);
        JList commitMessages = new JBList();
        commitMessages.setListData(commitMessagesList.toArray());
        changesPanel.add(subTitle);
        changesPanel.add(commitMessages);

        //JOptionPane.showMessageDialog(frame, subtitleString  + commitMessages, titleString, JOptionPane.INFORMATION_MESSAGE);
    }
}
