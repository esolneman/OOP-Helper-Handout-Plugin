package gui;

import controller.LoggingController;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import static environment.Messages.*;

//Notification for displaying information and link to questionnaire
public class QuestionnaireDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JEditorPane linkText;
    private static String CONTENT_TYPE = "text/html";
    //needed to bind to form file
    private JPanel dialogDescriptionPanel;

    public QuestionnaireDialog() {
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        initDescriptionText();
        inithyperLinkListener();
        initButtons();
        setContentPane(contentPane);
    }

    private void initButtons() {
        buttonOK.addActionListener(e -> onOK());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void inithyperLinkListener() {
        linkText.addHyperlinkListener(e -> {
            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void initDescriptionText() {
        this.setTitle(QUESTIONNAIRE_TITLE);
        linkText.setContentType(CONTENT_TYPE);
        //https://stackoverflow.com/questions/8348063/clickable-links-in-joptionpane
        linkText.setText(QUESTIONNAIRE_DESCRIPTION_START + QUESTIONNAIRE_URL_BASE + LoggingController.getInstance().getCurrentUser().getID() + QUESTIONNAIRE_DESCRIPTION_END);
        linkText.setEditable(false);
    }

    private void onOK() {
        setVisible(false);
    }

    private void onCancel() {
        setVisible(false);
    }

    public static void main(String[] args) {
        QuestionnaireDialog dialog = new QuestionnaireDialog();
        dialog.pack();
        dialog.setMinimumSize(new Dimension(500, 250));
        //center dialog window on screen https://stackoverflow.com/a/213291
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
