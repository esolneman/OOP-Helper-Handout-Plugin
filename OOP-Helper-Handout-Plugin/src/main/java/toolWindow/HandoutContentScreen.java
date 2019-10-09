package toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import environment.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;

import java.io.File;
import java.net.MalformedURLException;

public class HandoutContentScreen {
    private HandoutPluginFXPanel handoutContent;
    private WebView webView;
    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;

    public HandoutContentScreen(ToolWindow toolWindow){
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
       /* ActionToolbar toolbar;
        ActionToolbar handoutToolbar;
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(true) ;
        handoutToolbar.setTargetComponent(panel);
        panel.setToolbar(handoutToolbar.getComponent());
        handoutContent.add(panel);*/
         //ActionManager.getInstance().createActionToolbar();
        //com.intellij.openapi.actionSystem.ActionManager.createActionToolbar ` and add AnAction on it via ActionGroup.
        //ToolWindowActionMAnager
        /*ActionGroup handoutActionGroup = new ActionGroup() {
            @NotNull
            @Override
            public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
                return new AnAction[0];
            }
        };
        ActionManager actionManager = ActionManager.getInstance();
        actionManager.createButtonToolbar("help", handoutActionGroup );*/
        /*ActionToolbar actionToolbar = (ActionToolbar) this.handoutContent;
        actionToolbar.setOrientation(SwingConstants.HORIZONTAL);
        //handoutContent.remove();
        handoutContent.add(actionToolbar.getComponent(), BorderLayout.PAGE_START);*/
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

    public JFXPanel getContent() {
        return handoutContent;
    }


}
