package gui;

import controller.NotesController;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import org.jsoup.nodes.Document;
import toolWindow.NotesScreen;

public class NoteAddingFrame {
    private HTMLEditor htmlEditor;
    private Stage addNoteFrame = new Stage();
    private NotesScreen notesScreen;
    private NotesController notesController;
    private static NoteAddingFrame single_instance = null;


    public static NoteAddingFrame getInstance() {
        if (single_instance == null) {
            single_instance = new NoteAddingFrame();
        }
        return single_instance;
    }

    private NoteAddingFrame() {
        notesController = NotesController.getInstance();
        createHtmlEditor();
        //showAddNoteFrame();
    }

    public void setNotesScreen(NotesScreen notesScreen){
        this.notesScreen = notesScreen;
    }

    //https://docs.oracle.com/javafx/2/ui_controls/editor.htm
    private void createHtmlEditor() {
        htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(400);
        Document notesDocument = notesController.getCurrentNotesDocument();
        //display html part, that contains the notes
        htmlEditor.setHtmlText(notesDocument.getElementById("notesList").toString());
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setStyle("-fx-background-color: white");
        scrollPane.setContent(htmlEditor);
        scrollPane.setFitToWidth(true);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        Button addEntryButton = new Button("OK");
        root.setAlignment(Pos.CENTER);
        addEntryButton.setOnAction(event -> {
            NotesController.saveNewEntryInFile(htmlEditor.getHtmlText());
            addNoteFrame.close();
            notesScreen.reloadWebView();
        });

        //addNoteFrame.setSize(400,400);

        root.getChildren().addAll(scrollPane,addEntryButton);
        Scene scene = new Scene(new Group());
        scene.setRoot(root);
        addNoteFrame.setScene(scene);
    }

    //called from html
    public void showAddNoteFrame(){
        System.out.println("showAddNoteFrame");

        addNoteFrame.show();
    }


}
