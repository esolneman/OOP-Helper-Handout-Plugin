package gui;

import com.intellij.execution.process.ProcessHandler;
import controller.LoggingController;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URISyntaxException;

import static environment.Messages.*;

public class QuestionnaireDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel dialogDescriptionPanel;
    private JEditorPane linkText;

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
        linkText.setContentType("text/html");
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
