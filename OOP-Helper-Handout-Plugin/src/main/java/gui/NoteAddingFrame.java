package gui;

import controller.LoggingController;
import controller.NotesController;
import de.ur.mi.pluginhelper.logger.LogDataType;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import org.w3c.dom.Document;
import toolWindow.NotesScreen;

public class NoteAddingFrame {
    private HTMLEditor htmlEditor;
    private Stage addNoteFrame = new Stage();
    private NotesScreen notesScreen;
    private NotesController notesController;
    private static NoteAddingFrame single_instance = null;
    private LoggingController loggingController;
    private static String OK_BUTTON_LABEL = "OK";

    public static NoteAddingFrame getInstance() {
        if (single_instance == null) {
            single_instance = new NoteAddingFrame();
        }
        return single_instance;
    }

    private NoteAddingFrame() {
        notesController = NotesController.getInstance();
        loggingController = LoggingController.getInstance();
        createHtmlEditor();
    }

    public void setNotesScreen(NotesScreen notesScreen){
        this.notesScreen = notesScreen;
    }

    //https://docs.oracle.com/javafx/2/ui_controls/editor.htm
    private void createHtmlEditor() {
        htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(400);
        WebView webView = notesController.getCurrentWebview();
        //display html part, that contains the notes
        htmlEditor.setHtmlText((String)webView.getEngine().executeScript("document.getElementById('notesList').innerHTML"));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setStyle("-fx-background-color: white");
        scrollPane.setContent(htmlEditor);
        scrollPane.setFitToWidth(true);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        //TODO Constant
        Button addEntryButton = new Button(OK_BUTTON_LABEL);
        root.setAlignment(Pos.CENTER);
        addEntryButton.setOnAction(event -> {
            NotesController.getInstance().saveNewEntryInFile(htmlEditor.getHtmlText());
            loggingController.saveDataInLogger(LogDataType.NOTES, "Notes Edited", "htmlEditor.getHtmlText()");
            addNoteFrame.close();
            notesScreen.reloadWebView();
        });
        root.getChildren().addAll(scrollPane,addEntryButton);
        Scene scene = new Scene(new Group());
        scene.setRoot(root);
        addNoteFrame.setScene(scene);
    }

    //called from html
    public void showAddNoteFrame(){
        loggingController.saveDataInLogger(LogDataType.NOTES, "Notes Editing Frame", "open");
        addNoteFrame.show();
    }
}
