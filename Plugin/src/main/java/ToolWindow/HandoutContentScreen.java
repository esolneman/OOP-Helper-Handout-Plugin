package ToolWindow;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class HandoutContentScreen {
    private JPanel handoutContent;

    public HandoutContentScreen(ToolWindow toolWindow) {
        //hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
    }

    public JPanel getContent() {
        return handoutContent;
    }

}
