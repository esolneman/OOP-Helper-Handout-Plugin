package gui;

import controller.NotesController;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
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
}
