package webView;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import provider.RepoLocalStorageDataProvider;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class WebViewLinkListener {

    private WebView webView;
    String urlString;

    public WebViewLinkListener(WebView webView, String urlString) {
        this.webView = webView;
        this.urlString = urlString;
        createListener();
    }

    //TODO: depractaion!!!
    //https://stackoverflow.com/questions/15555510/javafx-stop-opening-url-in-webview-open-in-browser-instead
    //https://stackoverflow.com/questions/31909455/open-hyperlinks-in-javafx-webview-with-default-browser
    private void createListener() {
        webView.getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
            //String toBeopen = webView.getEngine().getLoadWorker().getMessage().trim();
            String toBeopen = observable.getValue();
            Project project = RepoLocalStorageDataProvider.getProject();
            if ((toBeopen.contains("class/") || (toBeopen.contains("method/")))) {
                handleLinkToCode(toBeopen, project);
            } else {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    URI address = new URI(observable.getValue());
                    if (toBeopen.contains("http://") || toBeopen.contains("https://") || toBeopen.contains("mailto")) {
                        Platform.setImplicitExit(false);
                        Platform.runLater(() -> {
                            webView.getEngine().load(urlString);
                        });
                        desktop.browse(address);
                    }
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void handleLinkToCode(String toBeopen, Project project) {
        int finalMethodLineNumber = 1;
        VirtualFile newFile = null;
        String classDirectory;
        //TODO: other name for var:
        String lineToSelect = "";
        String pathToClass = "";
        if (toBeopen.contains("class/")) {
            //https://stackoverflow.com/a/17113365
            classDirectory = toBeopen.split("(?<=class)")[1];
            newFile = LocalFileSystem.getInstance().findFileByPath(RepoLocalStorageDataProvider.getUserProjectDirectory() + classDirectory);
            pathToClass = RepoLocalStorageDataProvider.getUserProjectDirectory() + classDirectory;
            lineToSelect = newFile.getName().substring(0,newFile.getName().indexOf("."));
            System.out.println(lineToSelect);
            //TODO: ADD Ballon for unable to find class
        } else if (toBeopen.contains("method/")) {
            classDirectory = toBeopen.split("(?<=method)")[1];
            Integer findLastSlash = classDirectory.lastIndexOf("/");
            classDirectory = classDirectory.substring(0, findLastSlash);
            lineToSelect = toBeopen.split("(?<=/)")[toBeopen.split("(?<=/)").length -1];
            pathToClass = RepoLocalStorageDataProvider.getUserProjectDirectory() + classDirectory;
            newFile = LocalFileSystem.getInstance().findFileByPath(pathToClass);
        }
        File file = new File(pathToClass);
        //https://stackoverflow.com/a/5600442
        try {
            Scanner scanner = new Scanner(file);
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if(line.contains( " " + lineToSelect)) {
                    finalMethodLineNumber = lineNum;
                    break;
                }
            }
        } catch(FileNotFoundException e) {
            //handle this
        }
        VirtualFile finalNewFile = newFile;
        int finalMethodLineNumber1 = finalMethodLineNumber;
        ApplicationManager.getApplication().invokeLater(() -> {
            //https://intellij-support.jetbrains.com/hc/en-us/community/posts/206113019-Open-a-Class-programmatically-in-a-new-Editor
            FileEditorManager.getInstance(project).openFile(finalNewFile, true, true);
            final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
            final FileEditor[] editor = fileEditorManager.openFile(finalNewFile, true);
            final Editor textEditor = ((TextEditor) editor[0]).getEditor();
            final LogicalPosition problemPos = new LogicalPosition(finalMethodLineNumber1 - 1, 0);
            textEditor.getCaretModel().moveToLogicalPosition(problemPos);
            textEditor.getSelectionModel().selectLineAtCaret();
            textEditor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
        });
    }





}
