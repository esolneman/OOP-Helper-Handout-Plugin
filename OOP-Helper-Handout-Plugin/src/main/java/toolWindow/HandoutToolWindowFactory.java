package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import eventHandling.OnToolWindowCreatedListener;
import services.ToolWindowServiceInterface;

import java.util.ArrayList;
import java.util.List;


public class HandoutToolWindowFactory implements ToolWindowFactory, ToolWindowServiceInterface {

    private List<OnToolWindowCreatedListener> listeners = new ArrayList<>();

    private ToolWindow toolWindow;
    private ContentFactory contentFactory;

    private Content handoutContent;
    private Content checklistContent;
    private Content shortcutContent;
    private Content specificCriteriaContent;
    private Content commonAssessmentCriteriaContent;
    private Content commonAssessmentCriteriaContentJavaFXTable;


    private HandoutContentScreen handoutContentScreen;
    private ChecklistScreen checklistScreen;
    private ShortcutScreen shortcutScreen;
    private SpecificAssessmentCriteriaScreen specificAssessmentCriteria;
    private CommonAssessmentCriteriaScreen commonAssessmentCriteriaScreen;
    private AssessmentScreen assessmentScreen;

    // Create the tool window content.
    // is called when user clicks on tool window button
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        contentFactory = ContentFactory.SERVICE.getInstance();
        initScreens();
        createToolbar();
    }

    private void initScreens() {
        handoutContentScreen = new HandoutContentScreen(toolWindow);
        checklistScreen = new ChecklistScreen(toolWindow);
        shortcutScreen = new ShortcutScreen(toolWindow);
        //specificAssessmentCriteria = new SpecificAssessmentCriteriaScreen(toolWindow);
        commonAssessmentCriteriaScreen = new CommonAssessmentCriteriaScreen(toolWindow);
        assessmentScreen = new AssessmentScreen(toolWindow);
        addScreenContent();
    }

    private void addScreenContent() {
        handoutContent = contentFactory.createContent(handoutContentScreen.getContent(), "HandoutHTML", false);
        handoutContent.setPreferredFocusableComponent(handoutContentScreen.getContent());
        checklistContent = contentFactory.createContent(checklistScreen.getContent(), "Checklist", false);
        shortcutContent = contentFactory.createContent(shortcutScreen.getContent(), "Shortcut", false);
        //specificCriteriaContent = contentFactory.createContent(specificAssessmentCriteria.getContent(), "Specific Assessment Criteria", false);
        commonAssessmentCriteriaContent = contentFactory.createContent(commonAssessmentCriteriaScreen.getContent(), "Common Assessment Criteria", false);
        commonAssessmentCriteriaContentJavaFXTable = contentFactory.createContent(assessmentScreen.getContent(), "TABLE", false);

        toolWindow.getContentManager().addContent(handoutContent);
        toolWindow.getContentManager().addContent(checklistContent);
        toolWindow.getContentManager().addContent(shortcutContent);
        //toolWindow.getContentManager().addContent(specificCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContentJavaFXTable);
        //TODO: Decide which Tab is open when start ide
        toolWindow.getContentManager().setSelectedContent(handoutContent);
        callListener();

    }

    //https://www.programcreek.com/java-api-examples/?api=com.intellij.openapi.wm.ex.ToolWindowEx
    private void createToolbar() {
        AnAction updateAction = ActionManager.getInstance().getAction("Myplugin.Textboxes.Update");
        AnAction minimizeAction = (ActionManager.getInstance().getAction("Handout.Minimize"));
        AnAction maximizeAction = (ActionManager.getInstance().getAction("Handout.Maximize"));
        ((ToolWindowEx) toolWindow).setTitleActions(new AnAction[]{updateAction, minimizeAction, maximizeAction});
    }

    public void updateContent() {
        System.out.println("Update ToolWindows");
        handoutContentScreen.updateContent();
        assessmentScreen.updateContent();
    }

    @Override
    public void addListener(OnToolWindowCreatedListener listener) {
        listeners.add(listener);
    }

    private void callListener() {
        System.out.println("toolWindow available");
        if (listeners != null) {
            System.out.println("listener not null");
            for (OnToolWindowCreatedListener listener : listeners) {
                listener.OnToolWindowCreatedEvent(this);
            }
        }
    }
}
