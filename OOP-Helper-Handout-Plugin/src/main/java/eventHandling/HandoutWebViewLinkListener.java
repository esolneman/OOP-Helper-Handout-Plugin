package eventHandling;

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
import controller.LoggingController;
import de.ur.mi.pluginhelper.logger.LogDataType;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.codefx.libfx.control.webview.WebViewHyperlinkListener;
import org.codefx.libfx.control.webview.WebViews;
import provider.RepoLocalStorageDataProvider;

import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

import static environment.LoggingMessageConstants.HANDOUT_LINK_TO_CODE;
import static environment.LoggingMessageConstants.HANDOUT_LINK_TO_EXTERNAL_PAGE;

public class HandoutWebViewLinkListener {

    private WebView webView;
    private String urlString;
    private static String HTTP_PREFIX = "http://";
    private static String HTTPS_PREFIX = "https://";
    private static String MAIL_PREFIX = "mailto";
    private static String LINK_TO_CODE_ID = "LinkToCode";
    private int finalMethodLineNumber = 1;
    private VirtualFile newFile = null;
    private String className = "";
    private String lineToSelect = "";
    private String pathToClass = "";

    public HandoutWebViewLinkListener(WebView webView, String urlString) {
        this.webView = webView;
        this.urlString = urlString;
    }

    public void createListener() {
        //https://github.com/CodeFX-org/LibFX/wiki/WebViewHyperlinkListener
        WebViewHyperlinkListener eventPrintingListener = event -> {
            String hyperlink = event.getURL().toString();
            Project project = RepoLocalStorageDataProvider.getProject();
            if (hyperlink.contains(LINK_TO_CODE_ID)) {
                LoggingController.getInstance().saveDataInLogger(LogDataType.HANDOUT, HANDOUT_LINK_TO_CODE, hyperlink);
                handleLinkToCode(hyperlink, project);
            } else {
                LoggingController.getInstance().saveDataInLogger(LogDataType.HANDOUT, HANDOUT_LINK_TO_EXTERNAL_PAGE, hyperlink);
                handleLinkToExternalWebpage(hyperlink);
            }
            return false;
        };
        WebViews.addHyperlinkListener(webView, eventPrintingListener, HyperlinkEvent.EventType.ACTIVATED);
    }

    //open link in external browser and reload webview
    private void handleLinkToExternalWebpage(String hyperLink) {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI address = new URI(hyperLink);
            if (hyperLink.contains(HTTP_PREFIX) || hyperLink.contains(HTTPS_PREFIX) || hyperLink.contains(MAIL_PREFIX)) {
                Platform.setImplicitExit(false);
                Platform.runLater(() -> {
                    webView.getEngine().reload();
                });
                desktop.browse(address);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLinkToCode(String toBeopen, Project project) {
        //https://stackoverflow.com/a/13592324
        List<org.apache.http.NameValuePair> params = null;
        try {
            params = URLEncodedUtils.parse(new URI(toBeopen), Charset.forName("UTF-8"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        for (NameValuePair param : params) {
            switch(param.getName()) {
                case "class":
                    className = param.getValue();
                    break;
                case "path":
                    pathToClass = param.getValue();
                    break;
                case "function":
                    lineToSelect = param.getValue();
                    break;
                default:
            }
        }
        getLineNumber();
        openClass(project);

    }

    //get line number from method, the link points to
    private void getLineNumber(){
        newFile = LocalFileSystem.getInstance().findFileByPath(RepoLocalStorageDataProvider.getUserProjectDirectory() + pathToClass + className);
        pathToClass = RepoLocalStorageDataProvider.getUserProjectDirectory() + pathToClass + className;
        File file = new File(pathToClass);
        //https://stackoverflow.com/a/5600442
        try {
            Scanner scanner = new Scanner(file);
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if(line.contains(lineToSelect)) {
                    finalMethodLineNumber = lineNum;
                    break;
                }
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //open class and scrolls to line from the method, the link points to
    private void openClass(Project project){
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
