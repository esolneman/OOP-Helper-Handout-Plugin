package gui;

import controller.LoggingController;
import controller.NotesController;
import de.ur.mi.pluginhelper.logger.LogDataType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.stage.WindowEvent;
import org.w3c.dom.Document;
import toolWindow.NotesScreen;

import static environment.LoggingMessageConstants.*;

public class NoteAddingFrame {
    private HTMLEditor htmlEditor;
    private Stage addNoteFrame = new Stage();
    private NotesScreen notesScreen;
    private NotesController notesController;
    private static NoteAddingFrame single_instance = null;
    private LoggingController loggingController;
    private static String OK_BUTTON_LABEL = "Speichern";
    private static String EDIT_FRAME_TITLE = "Editiere die Notizen";

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
        setCancelRequest();
    }

    private void setCancelRequest() {
        System.out.println("FAGFG");
        addNoteFrame.setOnCloseRequest(event -> {
            event.consume();
            //Stage init
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            // Frage - Label
            Label label = new Label("Deine Änderungen werden verworfen. Willst du wirklich die Bearbeitung abbrechen?");
            // Antwort-Button JA
            Button okBtn = new Button("Ja");
            okBtn.setOnAction(event12 -> {
                addNoteFrame.hide();
                dialog.close();
            });

            // Antwort-Button NEIN
            Button cancelBtn = new Button("Nein");
            cancelBtn.setOnAction(event1 -> dialog.close());
            // Layout fuer Button
            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.setAlignment(Pos.CENTER);
            hbox.getChildren().add(okBtn);
            hbox.getChildren().add(cancelBtn);


            // Layout fuer Label und hBox
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(10);
            vbox.getChildren().add(label);
            vbox.getChildren().add(hbox);


            // Stage befuellen
            Scene scene = new Scene(vbox);
            dialog.setScene(scene);
            dialog.show();
        });
    }

    public void setNotesScreen(NotesScreen notesScreen) {
        this.notesScreen = notesScreen;
    }

    //https://docs.oracle.com/javafx/2/ui_controls/editor.htm
    private void createHtmlEditor() {
        htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(400);
        WebView webView = notesController.getCurrentWebview();
        //display html part, that contains the notes
        htmlEditor.setHtmlText((String) webView.getEngine().executeScript("document.getElementById('notesList').innerHTML"));
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
            loggingController.saveDataInLogger(LogDataType.NOTES, NOTES_EDITED, NOTES_EDITED);
            addNoteFrame.close();
            notesScreen.reloadWebView();
        });
        root.getChildren().addAll(scrollPane, addEntryButton);
        root.setSpacing(5);
        root.setPadding(new Insets(0, 0, 5, 0));
        Scene scene = new Scene(new Group());
        scene.setRoot(root);
        addNoteFrame.setTitle(EDIT_FRAME_TITLE);
        addNoteFrame.setScene(scene);
    }

    //called from html
    public void showAddNoteFrame() {
        loggingController.saveDataInLogger(LogDataType.NOTES, NOTES_EDITING, OPEN_NOTES_EDITING);
        addNoteFrame.show();
    }
}
