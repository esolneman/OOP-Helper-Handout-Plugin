package gui;

import com.sun.prism.impl.Disposer;
import controller.ChecklistController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;
import objects.ChecklistTableTask;

//TODO MAKE SINGLETON
public class ChecklistFXPanel extends JFXPanel {
    private ChecklistController checklistController;

    //https://docs.oracle.com/javafx/2/ui_controls/table-view.htm#CJAGAAEE
    public void showChecklistTable(ObservableList<ChecklistTableTask> predefinedData, ObservableList<ChecklistTableTask> userData) {
        Scene scene = new Scene(new Group());
        BorderPane border = new BorderPane();
        //GridPane checklistTablesPane = new GridPane();
        //border.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              (checklistTablesPane);
        VBox predefinedDataTable = getPredefinedDataTable(predefinedData);
        VBox userDataTable = getUserDataTable(userData);

        //https://stackoverflow.com/questions/35159841/javafx-centering-vbox-inside-gridpane
        // checklistTablesPane.add(predefinedDataTable, 0, 0);
        //checklistTablesPane.add(userDataTable, 1, 0);

        border.setLeft(predefinedDataTable);
        border.setRight(userDataTable);
        BorderPane.setAlignment(predefinedDataTable, Pos.CENTER_LEFT);
        BorderPane.setAlignment(userDataTable, Pos.CENTER_RIGHT);
        BorderPane.setMargin(userDataTable, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(predefinedDataTable, new Insets(10, 10, 10, 10));

        ((Group) scene.getRoot()).getChildren().addAll(border);

        this.setScene(scene);
        this.show();
    }

    private VBox getPredefinedDataTable(ObservableList<ChecklistTableTask> predefinedData) {
        TableView<ChecklistTableTask> predefinedTable;
        predefinedTable = createTable(predefinedData);
        predefinedTable.setEditable(true);


        predefinedTable.getColumns().get(0).setMaxWidth(1f * Integer.MAX_VALUE * 70); // 50% width
        predefinedTable.getColumns().get(1).setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30% width

        final Label predefinedDataLabel = new Label("Vordefinierte Checkliste");
        predefinedDataLabel.setFont(new Font("Arial", 20));
        final VBox userDataVBox = new VBox();
        userDataVBox.setSpacing(5);
        userDataVBox.setPadding(new Insets(10, 0, 0, 10));
        userDataVBox.getChildren().addAll(predefinedDataLabel, predefinedTable);
        return userDataVBox;
    }

    private VBox getUserDataTable(ObservableList<ChecklistTableTask> userData) {
        checklistController = ChecklistController.getInstance();
        final HBox hb = new HBox();
        final Label userDataLabel = new Label("Eigene Checkliste");
        userDataLabel.setFont(new Font("Arial", 20));
        TableView<ChecklistTableTask> userTable;
        userTable = createTable(userData);
        userTable.setEditable(true);

        TableColumn descriptionCol = userTable.getColumns().get(0);

        //https://gist.github.com/abhinayagarwal/9735744
        TableColumn deleteButtonCol = new TableColumn<>("Action");
        deleteButtonCol.setCellValueFactory(
                (Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>) p -> new SimpleBooleanProperty(p.getValue() != null));

        //Adding the Button to the cell
        deleteButtonCol.setCellFactory((Callback<TableColumn<Disposer.Record, Boolean>, ButtonCell>) p -> new ButtonCell(userData));

        userTable.getColumns().add(deleteButtonCol);

        //TODO aDD css to table
        //descriptionCol.setStyle("-fx-border-color: red;");

        //https://stackoverflow.com/a/7720885
        descriptionCol.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TextFieldTableCell call(TableColumn tableColumn) {
                return new TextFieldTableCell<ChecklistTableTask, String>() {
                    //("-fx-effect: dropshadow(three-pass-box, grey, 10, 0, 0, 10);");
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setStyle("-fx-underline: true ;");
                        }
                    }
                };
            }
        });

        //TextFieldTableCell.forTableColumn()


        /*https://stackoverflow.com/a/7720885
        descriptionCol.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn param) {
                return new TableCell<ChecklistTableTask, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setStyle( "-fx-underline: true ;");
                            // Get fancy and change color based on data
                            if(item.contains("@"))
                                this.setTextFill(Color.BLUEVIOLET);
                            setText(item);
                        }
                    }
                };
            }
        });*/


        //https://docs.oracle.com/javafx/2/ui_controls/table-view.htm#CEGFCFEB
        descriptionCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<ChecklistTableTask, String>>) checklistTableTaskStringCellEditEvent -> {
                    System.out.println("OLD VALUE: " + checklistTableTaskStringCellEditEvent.getOldValue());

                    checklistTableTaskStringCellEditEvent.getTableView()
                            .getItems().get(checklistTableTaskStringCellEditEvent.getTablePosition().getRow())
                            .setTaskDescription(checklistTableTaskStringCellEditEvent.getNewValue());
                    System.out.println("NEW VALUE: " + checklistTableTaskStringCellEditEvent.getNewValue());
                    checklistController.saveUserDataInFile(userData);
                });

        //https://stackoverflow.com/a/35265368
        descriptionCol.setMaxWidth(1f * Integer.MAX_VALUE * 60); // 50% width
        userTable.getColumns().get(1).setMaxWidth(1f * Integer.MAX_VALUE * 20); // 30% width
        deleteButtonCol.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 30% width

        //https://stackoverflow.com/a/13456063
        //Display delete Button in the center of the cell
        deleteButtonCol.setStyle("-fx-alignment: CENTER;");
        final TextField addDescription = new TextField();
        addDescription.setPromptText("Neue Aufgabe");
        addDescription.setMaxWidth(descriptionCol.getPrefWidth());
        //TODO ENTER POSSIBLE
        //addDescription.addEventHandler();

        final Button addButton = new Button("Add");
        addButton.setOnAction(actionEvent -> {
            String newDescription = addDescription.getText();
            if (!newDescription.isEmpty()) {
                userData.add(new ChecklistTableTask.TasksTableBuilder(addDescription.getText(), false).build());
                addDescription.clear();
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
        //TODO MAKE IT RESPONSIVE
        Double tableWidth = 300.00;
        System.out.println("PANEL WIDTH " + this.getWidth());
        System.out.println("TABLE WIDTH " + tableWidth);
        System.out.println("COL WIDTH " + tableWidth / 2);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        TableColumn taskDescriptionCol = new TableColumn("Aufgabe");
        taskDescriptionCol.setCellValueFactory(new PropertyValueFactory<ChecklistTableTask, String>("taskDescription"));

        TableColumn taskCheckedCol = new TableColumn("Erledigt");
        taskCheckedCol.setCellValueFactory(new PropertyValueFactory<ChecklistTableTask, Boolean>("checked"));

        //https://stackoverflow.com/a/12550850
        taskCheckedCol.setCellFactory(tc -> new CheckBoxTableCell());
/*
        taskCheckedCol.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<ChecklistTableTask, Boolean>>) checklistTableTaskBooleanCellEditEvent -> {
            System.out.println("CHECKED HANDLER");
            ChecklistController.saveTableDataInFile(data);
        });
*/
        taskCheckedCol.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            System.out.println("Cours " + data.get(param).getChecked());
            return data.get(param).checked;
        }));

        table.setItems(data);
        table.getColumns().addAll(taskDescriptionCol, taskCheckedCol);

        /*table.getItems().addListener((ListChangeListener<? super ChecklistTableTask>) change -> {
            System.out.println("DATA CHANGED");
        });*/

        return table;
    }

    //https://gist.github.com/abhinayagarwal/9735744
    class ButtonCell extends TableCell<Disposer.Record, Boolean> {
        final Button cellButton = new Button("X");

        ButtonCell(ObservableList<ChecklistTableTask> userData) {

            //adapt layout
            this.cellButton.setMaxHeight(5);
            this.cellButton.setMaxWidth(5);

            //Action when the button is pressed
            cellButton.setOnAction(actionEvent -> {
                // get Selected Item
                ChecklistTableTask currentTask = (ChecklistTableTask) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                //remove selected item from the table list
                userData.remove(currentTask);
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }
        }
    }
}
