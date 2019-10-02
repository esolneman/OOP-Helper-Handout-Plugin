package toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import provider.RepoLocalStorageDataProvider;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.io.File;
import java.io.IOException;

//import jdk.tools.jlink.internal.Platform;
//import javafx.embed.swing.JFXPanel;

public class HandoutContentScreen {
    private JPanel handoutContent;
    private JEditorPane assignmentDescription;
    File content;
    HTMLEditorKit kit;
    //private JFXPanelWrapper jfxWrapper;
    private String assignmentDescriptionString;
    public HandoutContentScreen(ToolWindow toolWindow){
        assignmentDescription.setContentType("text/html");
        assignmentDescription.setEditable(false);
        kit = new HTMLEditorKit();
        assignmentDescription.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        //styleSheet.addRule("h1 {color: blue;}");
        getHandoutData();
    }

    public void getHandoutData() {
        Document doc = kit.createDefaultDocument();
        assignmentDescription.setDocument(doc);
        content = RepoLocalStorageDataProvider.getHandoutHtmlFormat();
        try {
            assignmentDescription.setPage(content.toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public JPanel getContent() {
        return handoutContent;
    }

}
