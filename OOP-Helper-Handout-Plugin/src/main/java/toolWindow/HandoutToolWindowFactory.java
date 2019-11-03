package toolWindow;

import com.intellij.icons.AllIcons;
import com.intellij.ide.plugins.newui.MyPluginModel;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.content.*;
import environment.HandoutPluginFXPanel;
import listener.OnEventListener;
import org.jetbrains.annotations.NotNull;
import provider.HandoutContentDataProviderInterface;

import java.io.File;


public class HandoutToolWindowFactory implements ToolWindowFactory {
    ToolWindow toolWindow;
    ContentFactory contentFactory;

    Content handoutContent;
    Content checklistContent;
    Content shortcutContent;
    Content specificCriteriaContent;
    Content commonAssessmentCriteriaContent;

    HandoutContentScreen handoutContentScreen;
    ChecklistScreen checklistScreen;
    ShortcutScreen shortcutScreen;
    SpecificAssessmentCriteria specificAssessmentCriteria;
    CommonAssessmentCriteriaScreen commonAssessmentCriteriaScreen;

    // Create the tool window content.
    public void createToolWindowContent (Project project, ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        contentFactory = ContentFactory.SERVICE.getInstance();
        initScreens();
        createToolbar();
    }

    private void initScreens() {
        handoutContentScreen = new HandoutContentScreen(toolWindow);
        checklistScreen = new ChecklistScreen(toolWindow);
        shortcutScreen = new ShortcutScreen(toolWindow);
        specificAssessmentCriteria = new SpecificAssessmentCriteria(toolWindow);
        commonAssessmentCriteriaScreen = new CommonAssessmentCriteriaScreen(toolWindow);
        addScreenContent();
    }

    public void addScreenContent() {
        handoutContent = contentFactory.createContent(handoutContentScreen.getContent(), "HandoutHTML", false);
        handoutContent.setPreferredFocusableComponent(handoutContentScreen.getContent());
        checklistContent = contentFactory.createContent(checklistScreen.getContent(), "Checklist", false);
        shortcutContent = contentFactory.createContent(shortcutScreen.getContent(), "Shortcut", false);
        specificCriteriaContent = contentFactory.createContent(specificAssessmentCriteria.getContent(), "Specific Assessment Criteria", false);
        commonAssessmentCriteriaContent = contentFactory.createContent(commonAssessmentCriteriaScreen.getContent(), "Common Assessment Criteria", false);

        toolWindow.getContentManager().addContent(handoutContent);
        toolWindow.getContentManager().addContent(checklistContent);
        toolWindow.getContentManager().addContent(shortcutContent);
        toolWindow.getContentManager().addContent(specificCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContent);
        //TODO: Decide which Tab is open when start ide
        toolWindow.getContentManager().setSelectedContent(handoutContent);
    }

    //https://www.programcreek.com/java-api-examples/?api=com.intellij.openapi.wm.ex.ToolWindowEx
    private void createToolbar() {
        AnAction updateAction = ActionManager.getInstance().getAction("Myplugin.Textboxes.Update");
        AnAction minimizeAction = (ActionManager.getInstance().getAction("Handout.Minimize"));
        AnAction maximizeAction = (ActionManager.getInstance().getAction("Handout.Maximize"));
        ((ToolWindowEx)toolWindow).setTitleActions(new AnAction[] {updateAction, minimizeAction, maximizeAction});
    }
}
