package myToolWindow;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;


public class MyToolWindowTest {

    private JButton hideToolWindowButton;
    private JPanel myToolWindowContent;

    public MyToolWindowTest(ToolWindow toolWindow) {
        //hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

}
