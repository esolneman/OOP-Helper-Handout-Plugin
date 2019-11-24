package gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import toolWindow.NotesScreen;

public class PluginWebViewWithHeaderFXPanel extends JFXPanel {

    private BorderPane root = new BorderPane();
    private WebView webView;
    private String urlString;
    private String headerText;
    private Button btnTop;

    public void showWebView(String urlString, WebView webView, String headerText) {
        this.webView = webView;
        this.urlString = urlString;
        this.headerText = headerText;
        createBorderPane();
        setUpScene();
    }

    public void showWebViewWithTopSectionButton(String urlString, WebView webView, String headerText, NotesScreen notesScreen) {
        this.webView = webView;
        this.urlString = urlString;
        this.headerText = headerText;
        createBorderPane();
        //add EditButton
        Button notesEditButton = new Button("Edit Notes");
        notesEditButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Platform.setImplicitExit(false);
                Platform.runLater(() -> {
                    NoteAddingFrame noteAddingFrame = new NoteAddingFrame(notesScreen);
                    noteAddingFrame.showAddNoteFrame();
                });
            }
        });
        HBox notesTopSection = new HBox();
        notesTopSection.getChildren().addAll(btnTop, notesEditButton);
        notesEditButton.setAlignment(Pos.BASELINE_RIGHT);
        //notesTopSection.setAlignment(Pos.CENTER);
        root.setTop(notesTopSection);
        setUpScene();
    }

    private void createBorderPane() {
        System.out.println("in: showHandoutWebView");
        //Platform.setImplicitExit(false);
        //Platform.runLater(() -> {
        root = new BorderPane();
        //root.setPadding(new Insets(15, 20, 10, 10));
        // TOP
        btnTop = new Button(headerText);
        //btnTop.setPadding(new Insets(10, 10, 10, 10));
        root.setTop(btnTop);
        BorderPane.setAlignment(btnTop, Pos.CENTER);
        ;
        // Set margin for top area.
        BorderPane.setMargin(btnTop, new Insets(10, 10, 10, 10));
    }

    private void setUpScene() {
        root.setCenter(webView);
        webView.getEngine().load(urlString);
        webView.getEngine().setJavaScriptEnabled(true);
        setScene(new Scene(root));
    }


}


