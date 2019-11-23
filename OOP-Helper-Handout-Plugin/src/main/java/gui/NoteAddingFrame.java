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

//TODO make Singleton
public class NoteAddingFrame {
    private HTMLEditor htmlEditor;
    private Stage addNoteFrame = new Stage();
    private NotesScreen notesScreen;
    private NotesController notesController;

    public NoteAddingFrame(NotesScreen notesScreen) {
        notesController = NotesController.getInstance();
        this.notesScreen = notesScreen;
        createHtmlEditor();
    }

    //https://docs.oracle.com/javafx/2/ui_controls/editor.htm
    private void createHtmlEditor() {
        htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(400);
        Document notesDocument = notesController.getCurrentNotesDocument();
        htmlEditor.setHtmlText(notesDocument.toString());
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
            System.out.println(htmlEditor.getHtmlText());
            NotesController.saveNewEntryInFile(htmlEditor.getHtmlText());
            //htmlEditor.setHtmlText(notesDocument.toString());
            addNoteFrame.close();
            notesScreen.reloadWebView();
        });

        //addNoteFrame.setSize(400,400);

        root.getChildren().addAll(scrollPane,addEntryButton);
        Scene scene = new Scene(new Group());
        scene.setRoot(root);
        addNoteFrame.setScene(scene);
    }

    public void showAddNoteFrame(){
        addNoteFrame.show();
    }


}
