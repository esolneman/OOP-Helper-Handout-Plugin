package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManagerEvent;
import com.intellij.ui.content.ContentManagerListener;
import controller.LoggingController;
import de.ur.mi.pluginhelper.logger.LogDataType;
import eventHandling.OnToolWindowCreatedListener;
import org.jetbrains.annotations.NotNull;
import services.ToolWindowServiceInterface;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;


public class HandoutToolWindowFactory implements ToolWindowFactory, ToolWindowServiceInterface {

    private List<OnToolWindowCreatedListener> listeners = new ArrayList<>();

    private ToolWindow toolWindow;
    private ContentFactory contentFactory;

    private Content handoutContent;
    private Content refactoredChecklistContent;
   // private Content checklistContent;
    private Content specificCriteriaContent;
    private Content commonAssessmentCriteriaContent;
    private Content notesContent;


    private HandoutContentScreen handoutContentScreen;
    private RefactoresChecklistScreen refactoresChecklistScreen;
    //private ChecklistScreen checklistScreen;
    private SpecificAssessmentCriteriaScreen specificAssessmentCriteriaScreen;
    private HelpScreen commonAssessmentCriteriaScreen;
    private NotesScreen notesScreen;
    private LoggingController loggingController;

    // Create the tool window content.
    // is called when user clicks on tool window button
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        contentFactory = ContentFactory.SERVICE.getInstance();
        initScreens();
        createToolbar();
        createToolWindowListener();
        loggingController = LoggingController.getInstance();
    }

    private void createToolWindowListener() {
        toolWindow.getContentManager().addContentManagerListener(new ContentManagerListener() {
            @Override
            public void contentAdded(@NotNull ContentManagerEvent event) {
            }

            @Override
            public void contentRemoved(@NotNull ContentManagerEvent event) {
            }

            @Override
            public void contentRemoveQuery(@NotNull ContentManagerEvent event) {
            }

            @Override
            public void selectionChanged(@NotNull ContentManagerEvent event) {
                System.out.println("in: selectionChanged");
                if (event.getContent().isSelected()) {
                    System.out.println("Current Component Gained: \"" + event.getContent().getDisplayName() + "\"");
                    loggingController.saveDataInLogger(LogDataType.CUSTOM, "focus lost", event.getContent().getDisplayName());
                } else {
                    System.out.println("Current Component Lost: \"" + event.getContent().getDisplayName() + "\"");
                    loggingController.saveDataInLogger(LogDataType.CUSTOM, "focus gained", event.getContent().getDisplayName());
                }
            }
        });

/*        new FocusWatcher(){
            @Override
            protected void focusLostImpl(final FocusEvent e){
                System.out.println("toolWindow Lost Visi: \"" + toolWindow.getComponent().isVisible());
                System.out.println("toolWindow Lost: \"" + e.toString());
            }

            @Override
            protected void focusedComponentChanged(Component focusedComponent, @Nullable AWTEvent cause) {
                if (focusedComponent != null && SwingUtilities.isDescendingFrom(focusedComponent, toolWindow.getComponent())) {
                    System.out.println("toolWindow Gained: \"" + focusedComponent.isVisible());
                }
            }
        }.install(toolWindow.getComponent());*/

        toolWindow.getComponent().addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent ancestorEvent) {
                System.out.println("ancestorAdded toolWindow: " + toolWindow.isVisible());
                loggingController.saveDataInLogger(LogDataType.CUSTOM, "Tool Window Visibility", "closed");
            }

            @Override
            public void ancestorRemoved(AncestorEvent ancestorEvent) {
                System.out.println("ancestorRemoved toolWindow: " + toolWindow.isVisible());
                loggingController.saveDataInLogger(LogDataType.CUSTOM, "Tool Window Visibility", "opened");
            }

            @Override
            public void ancestorMoved(AncestorEvent ancestorEvent) {}
        });
    }

    private void initScreens() {
        handoutContentScreen = new HandoutContentScreen(toolWindow);
        refactoresChecklistScreen = new RefactoresChecklistScreen(toolWindow);
        //checklistScreen = new ChecklistScreen(toolWindow);
        specificAssessmentCriteriaScreen = new SpecificAssessmentCriteriaScreen(toolWindow);
        commonAssessmentCriteriaScreen = new HelpScreen(toolWindow);
        notesScreen = new NotesScreen(toolWindow);
        addScreenContent();
    }

    private void addScreenContent() {
        handoutContent = contentFactory.createContent(handoutContentScreen.getContent(), "Handout", false);
        handoutContent.setPreferredFocusableComponent(handoutContentScreen.getContent());
        refactoredChecklistContent = contentFactory.createContent(refactoresChecklistScreen.getContent(), "refCheck", false);
        //checklistContent = contentFactory.createContent(checklistScreen.getContent(), "Aufgaben", false);
        specificCriteriaContent = contentFactory.createContent(specificAssessmentCriteriaScreen.getContent(), "Bewertungskriterien", false);
        commonAssessmentCriteriaContent = contentFactory.createContent(commonAssessmentCriteriaScreen.getContent(), "Hilfe", false);
        notesContent = contentFactory.createContent(notesScreen.getContent(), "Notizen", false);

        toolWindow.getContentManager().addContent(handoutContent);
        toolWindow.getContentManager().addContent(refactoredChecklistContent);
        //toolWindow.getContentManager().addContent(checklistContent);
        toolWindow.getContentManager().addContent(specificCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContent);
        toolWindow.getContentManager().addContent(notesContent);
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
        //TODO Interface --> alle?
        handoutContentScreen.updateContent();
        specificAssessmentCriteriaScreen.updateContent();
        commonAssessmentCriteriaScreen.updateContent();
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
