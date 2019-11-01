package environment;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;
import objects.SpecificAssessmentCriteria;
import provider.LocalStorageDataProvider;

import java.util.Arrays;

public class HandoutPluginFXPanel extends JFXPanel {
    public void showHandoutWebView(String urlString, WebView webView) {
        //Platform.setImplicitExit(false);
        //Platform.runLater(() -> {
            webView.getEngine().load(urlString);
            webView.getEngine().setJavaScriptEnabled(true);
            setScene(new Scene(webView));
        //});
    }

    public void showContent() {
        System.out.println("in: showContent");
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            System.out.println("in: showContent (RunLater-Part)");
            Label helloWord = new Label();
            helloWord.setText("Hello World");
            this.setScene(new Scene(helloWord, 50, 50));
        });
    }

    public void showTable() {
        TableView table = new TableView();
        SpecificAssessmentCriteria data = LocalStorageDataProvider.getSpecificAssessmentCriteria();
        ObservableList data2 = FXCollections.observableArrayList();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            table.setEditable(true);
            for (String s : data.getHeadline()) {
                TableColumn newColumn = new TableColumn(s);
                table.getColumns().add(newColumn);
            }
            for (String[] criterion : data.getCriteria()) {
                table.getItems().add(Arrays.toString(criterion));
                TableRow newRow = new TableRow();
                newRow.setText(Arrays.toString(criterion));
                //newRow.updateTableView(table);
                data2.add(Arrays.toString(criterion));
                System.out.println("Criterion: " + Arrays.toString(criterion));
                System.out.println("data2: " + data2.get(0));
            }
            table.setItems(data2);

            this.setScene(new Scene(table, 50, 50));
        });
    }

    //public void
    public void sayName() {
            System.out.println("I am a MyJFXPanel");
        }
}
