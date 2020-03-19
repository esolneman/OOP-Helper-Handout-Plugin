package gui;

import controller.LoggingController;
import controller.NotesController;
import de.ur.mi.pluginhelper.logger.LogDataType;
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
    private static String SCRIPT_ADD_NOTES = "document.getElementById('notesList').innerHTML";
    private static String STYLE_CLASS_NO_BORDER = "noborder-scroll-pane";
    private static final String STYLE_COLOR_WHITE = "-fx-background-color: white";
    private static final String CANCEL_LABEL = "Willst du die Bearbeitung wirklich abbrechen? Deine \u00c4nderungen werden nicht gespeichert";


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

    //"JavaFX: Stage / Dialog beenden mit Abfrage" by Alexander Gräsel,
    // used under CC BY 3.0 DE / Desaturated from original
    private void setCancelRequest() {
        addNoteFrame.setOnCloseRequest(event -> {
            //prevent primary stage from closing: https://stackoverflow.com/a/23160810
            event.consume();

            //Stage init
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            Label label = new Label(CANCEL_LABEL);
            Button submitButton = new Button("Ja");
            submitButton.setOnAction(actionEvent -> {
                setTextContent();
                addNoteFrame.close();
                dialog.close();
            });

            Button cancelButton = new Button("Nein");
            cancelButton.setOnAction(actionEvent -> dialog.close());
            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.setAlignment(Pos.CENTER);
            hbox.getChildren().add(submitButton);
            hbox.getChildren().add(cancelButton);

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(10);
            vbox.getChildren().add(label);
            vbox.getChildren().add(hbox);

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
        setTextContent();
        //display html part, that contains the notes
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add(STYLE_CLASS_NO_BORDER);
        scrollPane.setStyle(STYLE_COLOR_WHITE);
        scrollPane.setContent(htmlEditor);
        scrollPane.setFitToWidth(true);
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
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

    private void setTextContent() {
        WebView webView = notesController.getCurrentWebview();
        htmlEditor.setHtmlText((String) webView.getEngine().executeScript(SCRIPT_ADD_NOTES));
    }

    //called from html
    public void showAddNoteFrame() {
        loggingController.saveDataInLogger(LogDataType.NOTES, NOTES_EDITING, OPEN_NOTES_EDITING);
        addNoteFrame.show();
    }
}
