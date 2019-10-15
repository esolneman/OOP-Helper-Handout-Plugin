package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import environment.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import provider.LocalStorageDataProvider;
import toolWindow.actionGroups.HandoutContentActionGroup;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

        });
    }

    //https://stackoverflow.com/questions/49070734/javafx-webview-link-to-anchor-in-document-doesnt-work-using-loadcontent
    public void goToLocation(String heading){
        //ToDO remove whitespaces from string
        System.out.println("goToLocation: " + heading);
        /*if(heading.contains(" ")){
            StringUtils.capitalize(heading);
            System.out.println("Capitalized: " + heading);

            //https://stackoverflow.com/a/15633284
            heading = heading.replaceAll("\\s+","");
            System.out.println("removedWhitespace: " + heading);
        }*/
        //String newLocation = urlString + "#" + heading;
        String newLocation = urlString  + heading;

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

    //
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
                    System.out.println(link.attr("href"));
                    // headings.add(link.text());
                    headings.add(link.attr("href"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return headings;
    }
}
