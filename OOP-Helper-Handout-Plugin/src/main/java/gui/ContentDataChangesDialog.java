package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static environment.Messages.CONTENT_CHANGED_TITLE;
import static java.nio.charset.StandardCharsets.UTF_8;

//Notification for changes in content data
public class ContentDataChangesDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JList list1;
    private ArrayList<String> commitMessages;
    private Integer numCommits;

    public ContentDataChangesDialog(ArrayList<String> commitMessages) {
        this.commitMessages = commitMessages;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        initDescriptionText();
        initCommitMessages();
        initButton();
    }

    private void initCommitMessages() {
        for (int i = 0; i < commitMessages.size(); i++) {
            byte[] commitMessagesBytes = commitMessages.get(i).getBytes();
            commitMessages.set(i, new String(commitMessagesBytes, UTF_8));
        }
        numCommits = commitMessages.size();
        list1.setListData(commitMessages.toArray());
    }

    private void initDescriptionText() {
        this.setTitle(CONTENT_CHANGED_TITLE);
    }

    private void initButton(){
        buttonOK.addActionListener(e -> onOK());
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        setVisible(false);
    }

    private void onCancel() {
        setVisible(false);
    }

    public static void main(ArrayList<String> commitMessages) {
        ContentDataChangesDialog dialog = new ContentDataChangesDialog(commitMessages);
        dialog.pack();
        dialog.setMaximumSize(new Dimension(400, 250));
        //center dialog window on screen https://stackoverflow.com/a/213291
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
