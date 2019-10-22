package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import environment.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.apache.commons.lang.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import toolWindow.actionGroups.HandoutContentActionGroup;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class HandoutContentScreen extends SimpleToolWindowPanel{
    private HandoutPluginFXPanel handoutContent;
    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;
    private static WebView webView;

    private SimpleToolWindowPanel toolWindowPanel;

    public HandoutContentScreen(ToolWindow toolWindow){
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        handoutToolWindow = toolWindow;
        content = LocalStorageDataProvider.getHandoutFileDirectory();
        try {
            urlString = content.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        createContent();
        initToolWindowMenu();
    }

    public static void setWebView(WebView webView) {
        HandoutContentScreen.webView = webView;
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(handoutContent);
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup handoutActionGroup = new DefaultActionGroup();
        HandoutContentActionGroup handoutContentActionGroup = (HandoutContentActionGroup) ActionManager.getInstance().getAction("Handout.TableOfContents");
        handoutContentActionGroup.setHandoutContentScreen(this);
        handoutActionGroup.add(handoutContentActionGroup);
        handoutActionGroup.add(new Separator());
        handoutActionGroup.add(ActionManager.getInstance().getAction("Handout.Download"));
        final ActionToolbar checklistActionToolbar = ActionManager.getInstance().createActionToolbar("HandoutTool", handoutActionGroup, true);
        return checklistActionToolbar.getComponent();
    }

    private void createContent() {
        handoutContent = new HandoutPluginFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = new WebView();
            handoutContent.showHandoutWebView(urlString, webView);
            setOnLinkListener();
        });
    }

    //https://stackoverflow.com/questions/49070734/javafx-webview-link-to-anchor-in-document-doesnt-work-using-loadcontent
    public void goToLocation(String heading){
        if(heading.contains(" ")){
            //https://stackoverflow.com/a/1892778
            heading = WordUtils.capitalize(heading);
            //https://stackoverflow.com/a/15633284
            heading = heading.replaceAll("\\s+","");
        }
        String newLocation = urlString + "#" + heading;
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView.getEngine().load(newLocation);
        });
    }

    public JComponent getToolbar(){
        return toolWindowPanel.getToolbar();
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }

    public ArrayList<String> getNavHeadings() {
        ArrayList<String> headings = new ArrayList<>();
        File htmlFile = LocalStorageDataProvider.getHandoutFileDirectory();
        Document yourPage;
        try {
            //https://stackoverflow.com/a/9611720
            yourPage = Jsoup.parse(htmlFile, null);
            //https://stackoverflow.com/questions/34392919/find-href-link-from-webpage-using-java
            Elements aElement = yourPage.select("a[href]");
            for (Element link : aElement) {
                if(link.attr("href").contains("#")){
                    headings.add(link.text());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return headings;
    }

    //https://stackoverflow.com/questions/15555510/javafx-stop-opening-url-in-webview-open-in-browser-instead
    //https://stackoverflow.com/questions/31909455/open-hyperlinks-in-javafx-webview-with-default-browser
    private void setOnLinkListener() {
        webView.getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
            //String toBeopen = webView.getEngine().getLoadWorker().getMessage().trim();
            String toBeopen = observable.getValue();
            Project project = RepoLocalStorageDataProvider.getProject();
            if (toBeopen.contains("class/")) {
                //https://stackoverflow.com/a/17113365
                String classDirectory = toBeopen.split("(?<=class)")[1];
                System.out.println("classDirectory: " + classDirectory);
                VirtualFile newFile = LocalFileSystem.getInstance().findFileByPath(RepoLocalStorageDataProvider.getUserProjectDirectory() + classDirectory);
                ApplicationManager.getApplication().invokeLater(() -> {
                    //https://intellij-support.jetbrains.com/hc/en-us/community/posts/206113019-Open-a-Class-programmatically-in-a-new-Editor
                    FileEditorManager.getInstance(project).openFile(newFile, true, true);
                });
                //TODO: ADD Ballon for unable to find class
            } else if (toBeopen.contains("method/")) {
                String classDirectory = toBeopen.split("(?<=method)")[1];
                Integer findLastSlash = classDirectory.lastIndexOf("/");
                classDirectory = classDirectory.substring(0, findLastSlash);
                String methodName = toBeopen.split("(?<=/)")[toBeopen.split("(?<=/)").length -1];
                String pathToClass = RepoLocalStorageDataProvider.getUserProjectDirectory() + classDirectory;
                VirtualFile newFile = LocalFileSystem.getInstance().findFileByPath(pathToClass);
                File file = new File(pathToClass);
                //https://stackoverflow.com/a/5600442
                int methodLineNumber = 0;
                try {
                    Scanner scanner = new Scanner(file);
                    int lineNum = 0;
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        lineNum++;
                        if(line.contains(methodName)) {
                            methodLineNumber = lineNum;
                            break;
                        }
                    }
                } catch(FileNotFoundException e) {
                    //handle this
                }
                int finalMethodLineNumber = methodLineNumber;
                ApplicationManager.getApplication().invokeLater(() -> {
                    //https://intellij-support.jetbrains.com/hc/en-us/community/posts/206113019-Open-a-Class-programmatically-in-a-new-Editor
                    //https://www.codota.com/code/java/methods/com.intellij.openapi.fileEditor.FileEditorManager/openFile?snippet=5ce6acf17e03440004e29f2f
                    final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
                    final FileEditor[] editor = fileEditorManager.openFile(newFile, true);
                    final Editor textEditor = ((TextEditor) editor[0]).getEditor();
                    final LogicalPosition problemPos = new LogicalPosition(finalMethodLineNumber - 1, 0);
                    textEditor.getCaretModel().moveToLogicalPosition(problemPos);
                    textEditor.getSelectionModel().selectLineAtCaret();
                    textEditor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
                    //TODO: ADD Ballon for unable to find class
                    //showMessage("Unable to find " + filename);
                });
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
}
