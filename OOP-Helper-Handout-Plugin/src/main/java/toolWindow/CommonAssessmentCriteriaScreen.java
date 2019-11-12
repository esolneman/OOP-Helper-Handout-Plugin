package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import gui.HandoutPluginFXPanel;

import javax.swing.*;

public class CommonAssessmentCriteriaScreen extends SimpleToolWindowPanel {
    private HandoutPluginFXPanel assessmentContent;
    private SimpleToolWindowPanel toolWindowPanel;



    public CommonAssessmentCriteriaScreen(ToolWindow toolWindow) {
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);


    }

    public JPanel getContent() {
        return toolWindowPanel;
    }

}
