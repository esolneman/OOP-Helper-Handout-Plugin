package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import environment.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

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

    /*
    * @link HandoutContentScreen#goToLocation
    */
    /*
    @see HandoutContentScreen#goToLocation
     */
    public ArrayList<String> getNavHeadings() {
        ArrayList<String> headings = new ArrayList<>();
        Document doc = null;
        File htmlFile = LocalStorageDataProvider.getHandoutFileDirectory();
        Document yourPage = null;
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
        webView.getEngine().locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, final String oldValue, String newValue) {
                Desktop d = Desktop.getDesktop();
                String toBeopen = webView.getEngine().getLoadWorker().getMessage().trim();
                if (toBeopen.contains("class.")) {
                    System.out.println("Link to class");
                    //VirtualFile newFile = VirtualFileManager.getInstance().findFileByUrl(RepoLocalStorageDataProvider.getHandoutHtmlString());
                    VirtualFile newFile = LocalFileSystem.getInstance().findFileByPath(RepoLocalStorageDataProvider.getUserProjectDirectory() + "/src/world/Player.java");
                    System.out.println("newFilePath: " + newFile.getPath());
                    Project project = RepoLocalStorageDataProvider.getProject();
                    //VirtualFile openFile = LocalFileSystem.getInstance().findFileByPath(RepoLocalStorageDataProvider.getHandoutHtmlString());
                    OpenFileDescriptor descriptor = new OpenFileDescriptor(project, newFile);
                    //FileEditorManager.getInstance(project).openTextEditor(descriptor, false);
                    //FileEditorManager.getInstance(project).openEditor(descriptor, true);
                    System.out.println("openFile Editor Single: " + FileEditorManager.getInstance(project).getSelectedEditor());
                    for (VirtualFile openFile : FileEditorManager.getInstance(project).getOpenFiles()) {
                        System.out.println("openFile For Each: " + openFile.getPath());
                    }
                    if(! (FileEditorManager.getInstance(project).isFileOpen(newFile))){
                        //Platform.setImplicitExit(false);
                        Platform.runLater(() -> {
                            FileEditorManager.getInstance(project).openFile(newFile, false);
                        });
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
}
