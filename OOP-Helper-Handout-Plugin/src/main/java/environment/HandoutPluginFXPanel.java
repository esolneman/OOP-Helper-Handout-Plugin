package environment;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import provider.RepoLocalStorageDataProvider;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HandoutPluginFXPanel extends JFXPanel {

    public void showHandoutWebView(String urlString, WebView webView) {
        //Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView.getEngine().load(urlString);
            webView.getEngine().setJavaScriptEnabled(true);
            setScene(new Scene(webView));
            setOnLinkListener(webView, urlString);
        });
    }

    //https://stackoverflow.com/questions/15555510/javafx-stop-opening-url-in-webview-open-in-browser-instead
    //https://stackoverflow.com/questions/31909455/open-hyperlinks-in-javafx-webview-with-default-browser
    private void setOnLinkListener(WebView webView, String urlString) {
        webView.getEngine().locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, final String oldValue, String newValue) {
                Desktop d = Desktop.getDesktop();
                String toBeopen = webView.getEngine().getLoadWorker().getMessage().trim();
                System.out.println(toBeopen);
                if (toBeopen.contains("class.")) {
                    System.out.println("Link to class");

                    //VirtualFile newFile = VirtualFileManager.getInstance().findFileByUrl(RepoLocalStorageDataProvider.getHandoutHtmlString());
                    VirtualFile newFile = LocalFileSystem.getInstance().findFileByPath(RepoLocalStorageDataProvider.getUserProjectDirectory() + "/src/world/Player.java");

                    System.out.println("newFilePath: " + newFile.getPath());
                    System.out.println("newFileType: " + newFile.getFileType());
                    Project project = RepoLocalStorageDataProvider.getProject();
                    //VirtualFile openFile = LocalFileSystem.getInstance().findFileByPath(RepoLocalStorageDataProvider.getHandoutHtmlString());
                    OpenFileDescriptor descriptor = new OpenFileDescriptor(project, newFile);
                    //FileEditorManager.getInstance(project).openTextEditor(descriptor, false);
                    //FileEditorManager.getInstance(project).openEditor(descriptor, true);
                    System.out.println(FileEditorManager.getInstance(project).isFileOpen(newFile));
                    System.out.println("openFile Editor Single: " + FileEditorManager.getInstance(project).getSelectedEditor());
                    for (VirtualFile openFile : FileEditorManager.getInstance(project).getOpenFiles()) {
                        System.out.println("openFile For Each: " + openFile.getPath());
                    }

                } else if (toBeopen.contains("method.")) {
                    System.out.println("Link to class");
                } else {
                    try {
                        System.out.println(observable);
                        URI address = new URI(observable.getValue());
                        if (toBeopen.contains("http://") || toBeopen.contains("https://") || toBeopen.contains("mailto")) {
                            Platform.setImplicitExit(false);
                            Platform.runLater(() -> {
                                webView.getEngine().load(urlString);
                            });
                            d.browse(address);
                        }
                    } catch (URISyntaxException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

    //public void
    public void sayName() {
            System.out.println("I am a MyJFXPanel");
        }
}
