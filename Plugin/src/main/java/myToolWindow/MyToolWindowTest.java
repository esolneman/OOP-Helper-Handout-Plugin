package myToolWindow;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;


public class MyToolWindowTest {

    private JButton refreshToolWindowButton;
    private JButton hideToolWindowButton;
    private JLabel currentDate;
    private JLabel currentTime;
    private JLabel timeZone;
    private JPanel myToolWindowContent;

    public MyToolWindowTest(ToolWindow toolWindow) {
        //hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

}
