package gui;

import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;


// J OPTION PANE
public class CommitChangesDialog {
    private JPanel changesPanel;
    private String titleString;
    private String subtitleString;
    private ArrayList<String> commitMessagesList;
    private Integer numCommits;
    private JDialog jDialog;


    //TODO REPLACE WITH CONSTANT MESSAGES
    public CommitChangesDialog(ArrayList<String> commitMessages) {
        commitMessagesList = commitMessages;
        for (int i = 0; i < commitMessagesList.size(); i++) {
            byte[] ptext = commitMessagesList.get(i).getBytes();
            commitMessagesList.set(i, new String(ptext, UTF_8));
        }
        numCommits = commitMessagesList.size();
        changesPanel = new JPanel();
        titleString = "Eine Aktuelle Version der Content Daten wurde heruntergeladen";
        subtitleString = numCommits + " Änderungen wurden hinzugefügt:";
        //https://stackoverflow.com/a/20243062
        byte[] ptext = subtitleString.getBytes();
        subtitleString = new String(ptext, UTF_8);
        createPanel();
    }


    public void showPanel() {
        //TODO replace woth dispatch thread
        JOptionPane.showMessageDialog(null, changesPanel, titleString, JOptionPane.INFORMATION_MESSAGE);
    }

    //https://stackoverflow.com/a/16267700
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
