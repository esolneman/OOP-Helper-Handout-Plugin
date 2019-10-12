package toolWindow;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.ui.JBDimension;
import com.intellij.util.ui.JBUI;
import environment.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;
import toolWindow.actionGroups.HandoutContentActionGroup;
import toolWindow.actions.HandoutTableOfContentsAction;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;

public class HandoutContentScreen extends SimpleToolWindowPanel {
    private HandoutPluginFXPanel handoutContent;
    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;
    private static WebView webView;

    private SimpleToolWindowPanel toolWindowPanel;

    public HandoutContentScreen(){
        super(true);
    };

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

    public static WebView getWebView() {
        return webView;
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

        /*checklistActionGroup.add(ActionManager.getInstance().getAction("Handout.TableOfContents"));
        checklistActionGroup.addSeparator();*/


        /*JBDimension myMinimumButtonSize = new JBDimension(16,16);
        Presentation buttonPresentation = new Presentation("icon");
        ActionButton tableOfContentsButton = new ActionButton((ActionManager.getInstance().getAction("Handout.TableOfContents")),
                buttonPresentation, "right",  myMinimumButtonSize);
        //tableOfContentsButton.setComponentPopupMenu();*/


        handoutActionGroup.add(ActionManager.getInstance().getAction("Handout.TableOfContents"));
        handoutActionGroup.add(new Separator());
        handoutActionGroup.addAll(new HandoutContentActionGroup());
        ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu("Headings", handoutActionGroup);
        handoutActionGroup.add(ActionManager.getInstance().getAction("Handout.Download"));
        final ActionToolbar checklistActionToolbar = ActionManager.getInstance().createActionToolbar("HandoutTool", handoutActionGroup, true);
        return checklistActionToolbar.getComponent();
    }

    private void createContent() {
        handoutContent = new HandoutPluginFXPanel();
        handoutContent.showHandoutWebView(urlString);
    }

    public void goToLocation(String heading){
        Field locationField = null;
        //WebView webView = handoutToolWindow.getComponent();
        try {
            System.out.println(webView.getEngine().getDocument().getDocumentURI());
            locationField = WebEngine.class.getDeclaredField(heading);
            locationField.setAccessible(true);
            ReadOnlyStringWrapper location = (ReadOnlyStringWrapper) locationField.get(webView.getEngine());
            location.set("local");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public JComponent getToolbar(){
        return toolWindowPanel.getToolbar();
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }


}
