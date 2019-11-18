package gui;

import controller.NotesController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import objects.Checklist;
import objects.SpecificAssessmentCriteria;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import provider.LocalStorageDataProvider;
import provider.contentHandler.ParseNotesJson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static environment.Messages.INITIAL_NOTES_TEXT;

public class HandoutPluginFXPanel extends JFXPanel {
    public void showHandoutWebView(String urlString, WebView webView) {
        System.out.println("in: showHandoutWebView");
        //Platform.setImplicitExit(false);
        //Platform.runLater(() -> {
            webView.getEngine().load(urlString);
            webView.getEngine().setJavaScriptEnabled(true);
            setScene(new Scene(webView));
        //});
    }

    //TODO Method for init HTML TEXT
    //https://docs.oracle.com/javafx/2/ui_controls/editor.htm
    public void showHTMLEditor(String urlString, WebView webView) {
        final HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(250);
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String html = INITIAL_NOTES_TEXT;
        //https://jsoup.org/cookbook/input/parse-document-from-string
        Document doc = Jsoup.parse(html);
        String date = formatter.format(currentDate);
        Element dateElement = doc.getElementById("date");
        dateElement.text(date);

        htmlEditor.setHtmlText(doc.toString());

        webView.getEngine().load(urlString);
        webView.getEngine().setJavaScriptEnabled(true);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setStyle("-fx-background-color: white");
        scrollPane.setContent(htmlEditor);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(new Group());

        Button addEntryButton = new Button("Neuer Eintrag");
        root.setAlignment(Pos.CENTER);
        addEntryButton.setOnAction(event -> {
            NotesController.saveNewEntryInFile(htmlEditor.getHtmlText());
            htmlEditor.setHtmlText(doc.toString());
            webView.getEngine().reload();
        });

        root.getChildren().addAll(webView, addEntryButton, scrollPane);

        scene.setRoot(root);
        this.setScene(scene);
        this.show();
    }

    //https://docs.oracle.com/javafx/2/ui_controls/table-view.htm#CJAGAAEE
    public void showChecklistTable( ObservableList<Checklist.Tasks> data, TableView table) {
        Scene scene = new Scene(new Group());
        final HBox hb = new HBox();
        final Label label = new Label("Vordefinierte Checkliste");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn taskDescriptionCol = new TableColumn("Aufgabe");
        taskDescriptionCol.setMinWidth(100);
        taskDescriptionCol.setCellValueFactory(
                new PropertyValueFactory<Checklist.Tasks, String>("taskDescription"));

        TableColumn taskChekcedCol = new TableColumn("Erledigt");
        taskChekcedCol.setMinWidth(100);
        taskChekcedCol.setCellValueFactory(
                new PropertyValueFactory<Checklist.Tasks, Boolean>("checked"));

        table.setItems(data);
        table.getColumns().addAll(taskDescriptionCol, taskChekcedCol);

        final TextField addDescription = new TextField();
        addDescription.setPromptText("Aufgabe");
        addDescription.setMaxWidth(taskDescriptionCol.getPrefWidth());


        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                data.add(new Checklist.Tasks.TasksBuilder(addDescription.getText(), false).build());
                addDescription.clear();
            }
        });

        hb.getChildren().addAll(addDescription, addButton);
        //TODO Maybe switch to 1 or 2
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        this.setScene(scene);
        this.show();
    }
}
