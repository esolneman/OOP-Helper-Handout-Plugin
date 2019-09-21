package ToolWindow;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.javafx.JFXPanelWrapper;
//import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HandoutContentScreen {
    private JPanel handoutContent;
    private JTextPane assignmentDescription;
    private JFXPanelWrapper jfxWrapper;
    private String assignmentDescriptionString;
    public HandoutContentScreen(ToolWindow toolWindow){
        //JFrame frame = new JFrame("FX");
        //JFXPanel fxPanel = new JFXPanel();
        //frame.add(fxPanel);
        //frame.setBounds(200, 100, 800, 250);
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //frame.setVisible(true);
        //WebView webView = new WebView();
        //hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
        //handoutContent
        getHandoutData();
    }

    public void getHandoutData() {
        //ToDo: add repoFile as paramter
        //String expectedValue = "Hello";
        //String file = repoFile.toString();
        String file = "C:/Masterarbeit/Starterpaket Beispiel/OOP-18WS-CoreDefense-Starter/RepoTEST/handout.md";

        try {
            DataInputStream reader = new DataInputStream(new FileInputStream(file));
            String contentToDisplay = null;
            contentToDisplay = reader.readUTF();
            reader.close();
            assignmentDescription.setText(contentToDisplay);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERRORRRRRR");
        }
    }

    public JPanel getContent() {
        return handoutContent;
    }

}
