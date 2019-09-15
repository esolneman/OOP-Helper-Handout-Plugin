package ToolWindow;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;


public class HandoutToolWindow {

    private JButton hideToolWindowButton;
    private JPanel myToolWindowContent;

    public HandoutToolWindow(ToolWindow toolWindow) {
        //hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

}
