package gui;

import controller.ChecklistController;
import controller.NotesController;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import objects.Checklist;
import objects.ChecklistTableTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
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
    public void showChecklistTable(ObservableList<ChecklistTableTask> predefinedData, ObservableList<ChecklistTableTask> userData) {
        Scene scene = new Scene(new Group());

        GridPane checklistTablesPane = new GridPane();
        VBox predefinedDataTable = getPredefinedDataTable(predefinedData);
        VBox userDataTable = getUserDataTable(userData);

        //https://stackoverflow.com/questions/35159841/javafx-centering-vbox-inside-gridpane
        checklistTablesPane.add(predefinedDataTable, 0, 0);
        checklistTablesPane.add(userDataTable, 1, 0);

        ((Group) scene.getRoot()).getChildren().addAll(checklistTablesPane);

        this.setScene(scene);
        this.show();
    }

    private VBox getPredefinedDataTable(ObservableList<ChecklistTableTask> predefinedData) {
        TableView<ChecklistTableTask> predefinedTable;
        predefinedTable = createTable(predefinedData);
        predefinedTable.setEditable(true);

        final Label predefinedDataLabel = new Label("Vordefinierte Checkliste");
        predefinedDataLabel.setFont(new Font("Arial", 20));
        final VBox userDataVBox = new VBox();
        userDataVBox.setSpacing(5);
        userDataVBox.setPadding(new Insets(10, 0, 0, 10));
        userDataVBox.getChildren().addAll(predefinedDataLabel, predefinedTable);
        return userDataVBox;
    }

    private VBox getUserDataTable(ObservableList<ChecklistTableTask> userData) {
        final HBox hb = new HBox();
        final Label userDataLabel = new Label("Eigene Checkliste");
        userDataLabel.setFont(new Font("Arial", 20));
        TableView<ChecklistTableTask> userTable = new TableView<>();
        userTable = createTable(userData);
        userTable.setEditable(true);

        TableColumn descriptionCol = userTable.getColumns().get(0);
        //TODO aDD css to table
        //descriptionCol.setStyle();
        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //https://docs.oracle.com/javafx/2/ui_controls/table-view.htm#CEGFCFEB
        descriptionCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<ChecklistTableTask, String>>) checklistTableTaskStringCellEditEvent -> {
                    System.out.println("OLD VALUE: " + checklistTableTaskStringCellEditEvent.getOldValue());
                    checklistTableTaskStringCellEditEvent.getTableView()
                            .getItems().get(checklistTableTaskStringCellEditEvent.getTablePosition().getRow()).setTaskDescription(checklistTableTaskStringCellEditEvent.getNewValue());
                    System.out.println("NEW VALUE: " + checklistTableTaskStringCellEditEvent.getNewValue());
                    ChecklistController.saveTableDataInFile(userData);
            });
        final TextField addDescription = new TextField();
        addDescription.setPromptText("Neue Aufgabe");
        addDescription.setMaxWidth(descriptionCol.getPrefWidth());
        //TODO ENTER POSSIBLE
        //addDescription.addEventHandler();

        final Button addButton = new Button("Add");
        addButton.setOnAction(actionEvent -> {
            String newDescription = addDescription.getText();
            if (!newDescription.isEmpty()) {
                //TODO save DATA in Json File :)
                userData.add(new ChecklistTableTask(addDescription.getText(), false));
                addDescription.clear();
                ChecklistController.saveTableDataInFile(userData);
            }

        });

        hb.getChildren().addAll(addDescription, addButton);
        //TODO Maybe switch to 1 or 2
        hb.setSpacing(1);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(userDataLabel, userTable, hb);

        return vbox;
    }

    private TableView<ChecklistTableTask> createTable(ObservableList<ChecklistTableTask> data) {
        TableView<ChecklistTableTask> table = new TableView<>();
        TableColumn taskDescriptionCol = new TableColumn("Aufgabe");
        taskDescriptionCol.setMinWidth(100);
        taskDescriptionCol.setCellValueFactory(new PropertyValueFactory<Checklist.Tasks, String>("taskDescription"));

        TableColumn taskCheckedCol = new TableColumn("Erledigt");
        taskCheckedCol.setMinWidth(100);
        taskCheckedCol.setCellValueFactory(new PropertyValueFactory<Checklist.Tasks, Boolean>("checked"));
        //https://stackoverflow.com/q/20879242
        taskCheckedCol.setCellFactory((Callback<TableColumn<Checklist.Tasks, Boolean>, TableCell<Checklist.Tasks, Boolean>>) p -> new CheckBoxTableCell<>());

        table.setItems(data);
        table.getColumns().addAll(taskDescriptionCol, taskCheckedCol);

        return table;
    }

}
