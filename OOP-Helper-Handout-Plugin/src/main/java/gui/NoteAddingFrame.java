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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import toolWindow.NotesScreen;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static environment.Messages.INITIAL_NOTES_TEXT;

public class NoteAddingFrame {
    private HTMLEditor htmlEditor;
    private Stage addNoteFrame = new Stage();
    private NotesScreen notesScreen;

    public NoteAddingFrame(NotesScreen notesScreen) {
        this.notesScreen = notesScreen;
        createHtmlEditor();
    }

    private void createHtmlEditor() {
        htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(400);
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String html = INITIAL_NOTES_TEXT;
        //https://jsoup.org/cookbook/input/parse-document-from-string
        Document doc = Jsoup.parse(html);
        String date = formatter.format(currentDate);
        Element dateElement = doc.getElementById("date");
        dateElement.text(date);
        htmlEditor.setHtmlText(doc.toString());
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setStyle("-fx-background-color: white");
        scrollPane.setContent(htmlEditor);
        scrollPane.setFitToWidth(true);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        Button addEntryButton = new Button("Neuer Eintrag");
        root.setAlignment(Pos.CENTER);
        addEntryButton.setOnAction(event -> {
            NotesController.saveNewEntryInFile(htmlEditor.getHtmlText());
            htmlEditor.setHtmlText(doc.toString());
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
