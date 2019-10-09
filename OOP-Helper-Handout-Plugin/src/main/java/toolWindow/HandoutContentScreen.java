package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import environment.HandoutPluginFXPanel;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;

public class HandoutContentScreen extends SimpleToolWindowPanel {
    private HandoutPluginFXPanel handoutContent;
    private WebView webView;
    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;

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

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(handoutContent);
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup checklistActionGroup = new DefaultActionGroup();
        final DefaultActionGroup newGroup;
        checklistActionGroup.add(ActionManager.getInstance().getAction("Handout.Download"));
        checklistActionGroup.add(ActionManager.getInstance().getAction("Handout.TableOfContents"));
        final ActionToolbar checklistActionToolbar = ActionManager.getInstance().createActionToolbar("Checklisttool", checklistActionGroup, true);
        return checklistActionToolbar.getComponent();
    }

    private void createContent() {
        handoutContent = new HandoutPluginFXPanel();
        handoutContent.showHandoutWebView(urlString);
    }


    public void updateContent(){
    /*    if(webView != null) {
            Platform.setImplicitExit(false);
            Platform.runLater(() -> {
                //webView.getEngine().load(urlString);
            });
        }*/
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }


}
