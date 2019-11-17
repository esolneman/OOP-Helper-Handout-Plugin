package gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import objects.SpecificAssessmentCriteria;
import provider.LocalStorageDataProvider;

import java.util.Arrays;

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

    public void showContent() {
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            Label helloWord = new Label();
            helloWord.setText("Hello World");
            this.setScene(new Scene(helloWord, 50, 50));
        });
    }

    public void showHTMLEditor() {
        final HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(245);
        Scene scene = new Scene(htmlEditor);
        this.setScene(scene);
        this.show();
    }

    //https://docs.oracle.com/javafx/2/ui_controls/editor.htm
    public void showHTMLEditor(String urlString, WebView webView) {
        final HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(250);

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

        root.getChildren().addAll(webView, scrollPane);
        scene.setRoot(root);
        this.setScene(scene);
        this.show();
    }
}
