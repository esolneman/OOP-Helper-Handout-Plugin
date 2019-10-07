package toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.JFXPanel;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import provider.RepoLocalStorageDataProvider;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ChecklistScreen {


    private JPanel checklistContent;
    private JPanel toolbar;
    private JPanel checklits;
    private JPanel repoChecklist;
    private JPanel userCheklist;
    private JTextPane textPane1;

    public ChecklistScreen(ToolWindow toolWindow) {
        File file = new File("C:/Masterarbeit/TestProjekt/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/checklist.md");
        textPane1.setText(file.getName());
    }

    public JPanel getChecklits() {
        System.out.println("Getting Content for checklist");

        return checklistContent;
    }


}
