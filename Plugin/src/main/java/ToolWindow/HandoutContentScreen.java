package ToolWindow;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.javafx.JFXPanelWrapper;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

//import jdk.tools.jlink.internal.Platform;
//import javafx.embed.swing.JFXPanel;
//import javafx.embed.swing.JFXPanel;

public class HandoutContentScreen {
    private JPanel handoutContent;
    private JTextPane assignmentDescription;
    //private JFXPanelWrapper jfxWrapper;
    private String assignmentDescriptionString;
    public HandoutContentScreen(ToolWindow toolWindow){

/*        JFXPanel jfxPanel = new JFXPanel();
        jFrame.add(jfxPanel);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            jfxPanel.setScene(new Scene(webView));
            webView.getEngine().load("http://www.stackoverflow.com/");
        });*/
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
        String file = "C:/Masterarbeit/Starterpaket Beispiel/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/handout.md";

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
