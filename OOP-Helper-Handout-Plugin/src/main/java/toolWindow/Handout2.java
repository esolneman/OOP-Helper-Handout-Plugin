package toolWindow;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Handout2 {
    private JPanel handoutContent;
    private JEditorPane assignmentDescription;

    public Handout2(ToolWindow toolWindow){
        assignmentDescription.setContentType("text/html");
        /*try {
            assignmentDescription.setPage("http://www.java.com");
        }
        catch (IOException ioe) {
            // HTML wird als Texttyp vorgegeben.

            // Text für Fehlermeldung wird
            // im HTML-Format übergeben.
            assignmentDescription.setText("<html> <center>"
                    + "<h1>Page not found</h1>"
                    + "</center> </html>.");
        }*/

        assignmentDescription.setEditable(false);
        //JBScrollPane scrollPane = new JBScrollPane(assignmentDescription);
        //assignmentDescription.add(scrollPane);
        //handoutContent.setSize(800, 600);
        //handoutContent.setVisible(true);
        getHandoutData();
    }

    public void getHandoutData() {
        //ToDo: add repoFile as paramter
        //String expectedValue = "Hello";
        //String file = repoFile.toString();
        //String file = "C:/Masterarbeit/TestProjekt/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/index.html";
        String file = "C:/Masterarbeit/TestProjekt/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/handout.md";

        try {
            DataInputStream reader = new DataInputStream(new FileInputStream(file));
            String contentToDisplay = null;
            contentToDisplay = reader.readUTF();
            reader.close();
            System.out.println("Content MD: ");
            System.out.println(contentToDisplay);

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
