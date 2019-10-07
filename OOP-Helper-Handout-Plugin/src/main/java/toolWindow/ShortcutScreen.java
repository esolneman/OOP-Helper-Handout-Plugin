package toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.javafx.JFXPanelWrapper;
import environment.HandoutPluginFXPanel;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;

public class ShortcutScreen {

    private HandoutPluginFXPanel shortcutContent;

    public ShortcutScreen(ToolWindow toolWindow)  {
        shortcutContent = new HandoutPluginFXPanel();
        shortcutContent.showContent();
    }

    public void reloadScreen() {

    }

    public JFXPanel getContent() {
        System.out.println("in: getContent (ShortcutScreen)");
        return shortcutContent;
    }

}
