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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import objects.Checklist;
import objects.ChecklistTableTask;
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
    public void showPredefinedChecklistTable(ObservableList<ChecklistTableTask> predefinedData, TableView table) {
        Scene scene = new Scene(new Group());

        System.out.println("ObservableList: " + predefinedData.get(0).taskDescription);
        System.out.println("ObservableList: " + predefinedData.get(0).taskDescription);



        final HBox hb = new HBox();
        final Label predefinedDataLabel = new Label("Vordefinierte Checkliste");
        predefinedDataLabel.setFont(new Font("Arial", 20));

        final Label userDataLabel = new Label("Eigene Checkliste");
        userDataLabel.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn taskDescriptionCol = new TableColumn("Aufgabe");
        taskDescriptionCol.setMinWidth(100);
        taskDescriptionCol.setCellValueFactory(new PropertyValueFactory<Checklist.Tasks, String>("taskDescription"));

        TableColumn taskCheckedCol = new TableColumn("Erledigt");
        taskCheckedCol.setMinWidth(100);
        taskCheckedCol.setCellValueFactory(new PropertyValueFactory<Checklist.Tasks, Boolean>("checked"));
        //https://stackoverflow.com/q/20879242
        taskCheckedCol.setCellFactory((Callback<TableColumn<Checklist.Tasks, Boolean>, TableCell<Checklist.Tasks, Boolean>>) p -> new CheckBoxTableCell<>());

        table.setItems(predefinedData);
        table.getColumns().addAll(taskDescriptionCol, taskCheckedCol);

        final TextField addDescription = new TextField();
        addDescription.setPromptText("Neue Aufgabe");
        addDescription.setMaxWidth(taskDescriptionCol.getPrefWidth());


        final Button addButton = new Button("Add");
        addButton.setOnAction(actionEvent -> {
            System.out.println("TEXT: " + addDescription.getText());
            predefinedData.add(new ChecklistTableTask(addDescription.getText(), false));
            addDescription.clear();
        });

        hb.getChildren().addAll(addDescription, addButton);
        //TODO Maybe switch to 1 or 2
        hb.setSpacing(1);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(predefinedDataLabel, table, hb);

        final VBox userDataVBox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(userDataLabel);

        ((Group) scene.getRoot()).getChildren().addAll(vbox, userDataVBox);

        System.out.println("TABLE COLS: "+ table.getColumns().size());

        this.setScene(scene);
        this.show();
    }

}
