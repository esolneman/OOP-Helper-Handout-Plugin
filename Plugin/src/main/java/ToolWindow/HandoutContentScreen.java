package ToolWindow;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.javafx.JFXPanelWrapper;
//import javafx.embed.swing.JFXPanel;

import javax.swing.*;

public class HandoutContentScreen {
    private JPanel handoutContent;
    private JFXPanelWrapper jfxWrapper;
    public HandoutContentScreen(ToolWindow toolWindow) {
        JFrame frame = new JFrame("FX");
        //JFXPanel fxPanel = new JFXPanel();
        //frame.add(fxPanel);
        frame.setBounds(200, 100, 800, 250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //WebView webView = new WebView();
        //hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
        //handoutContent
    }

    public JPanel getContent() {
        return handoutContent;
    }

}
