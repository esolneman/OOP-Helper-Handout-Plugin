package ToolWindow;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.javafx.JFXPanelWrapper;

import javax.swing.*;

public class ShortcutScreen {

    private JPanel shortcutContent;
    public ShortcutScreen(ToolWindow toolWindow) {

    }

    public JPanel getContent() {
            return shortcutContent;
        }

}
