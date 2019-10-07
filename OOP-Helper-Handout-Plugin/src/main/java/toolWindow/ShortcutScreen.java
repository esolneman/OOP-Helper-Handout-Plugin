package toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.javafx.JFXPanelWrapper;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javax.swing.*;

public class ShortcutScreen {

    private MyJFXPanel shortcutContent;

    public ShortcutScreen(ToolWindow toolWindow)  {
        shortcutContent = new MyJFXPanel();
        shortcutContent.showContent();
    }

    public void reloadScreen() {

    }

    public JFXPanel getContent() {
        System.out.println("in: getContent (ShortcutScreen)");
            return shortcutContent;
    }

}
