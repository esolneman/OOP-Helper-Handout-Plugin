package gui;

import com.intellij.openapi.wm.ToolWindowManager;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import toolWindow.NotesScreen;

public class PluginWebViewWithHeaderFXPanel extends JFXPanel {

    private BorderPane root = new BorderPane();
    private GridPane contentPane = new GridPane();
    private WebView webView;
    private String urlString;
    private String headerText;
    private Text header;
    private Group group;

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
        //HBox notesTopSection = new HBox();
        //notesTopSection.getChildren().addAll(header, notesEditButton);
        //notesEditButton.setAlignment(Pos.BASELINE_RIGHT);
        //notesEditButton.prefHeight(50);
        root.setRight(notesEditButton);
        BorderPane.setAlignment(notesEditButton, Pos.CENTER_RIGHT);
        //notesTopSection.setAlignment(Pos.CENTER);
        //root.setTop(notesTopSection);
        setUpScene();
    }

    private void createBorderPane() {
        System.out.println("in: showHandoutWebView");
        //Platform.setImplicitExit(false);
        //Platform.runLater(() -> {
        root = new BorderPane();
        //root.setPadding(new Insets(15, 20, 10, 10));
        // TOP


        header = new Text(headerText);
        //btnTop.setPadding(new Insets(10, 10, 10, 10));
        //header.maxHeight(50);
        root.setCenter(header);
        root.getCenter().prefHeight(10.00);
        BorderPane.setAlignment(header, Pos.CENTER);
        // Set margin for top area.
        contentPane.add(root, 0, 0);
        contentPane.add(webView, 0, 1);
        contentPane.setGridLinesVisible(true);
        group = new Group();
        group.getChildren().addAll(root, webView);
    }

    private void setUpScene() {
        //root.setBottom(webView);
        webView.getEngine().load(urlString);
        webView.getEngine().setJavaScriptEnabled(true);
        setScene(new Scene(group));
    }


}


