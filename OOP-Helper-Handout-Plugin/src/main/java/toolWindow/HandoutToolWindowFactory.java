package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.FocusWatcher;
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
import org.jetbrains.annotations.Nullable;
import services.ToolWindowServiceInterface;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;


public class HandoutToolWindowFactory implements ToolWindowFactory, ToolWindowServiceInterface {

    private List<OnToolWindowCreatedListener> listeners = new ArrayList<>();

    private ToolWindow toolWindow;
    private ContentFactory contentFactory;

    private Content handoutContent;
    private Content checklistContent;
    private Content specificCriteriaContent;
    private Content commonAssessmentCriteriaContent;
    private Content notesContent;


    private HandoutContentScreen handoutContentScreen;
    private ChecklistScreen checklistScreen;
    private SpecificAssessmentCriteriaScreen specificAssessmentCriteriaScreen;
    private HelpScreen helpScreen;
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
                if (event.getContent().isSelected()) {
                    loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW, "tab focus lost", event.getContent().getDisplayName());
                } else {
                    loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW, "tab focus gained", event.getContent().getDisplayName());
                }
            }
        });

        new FocusWatcher(){
            @Override
            protected void focusLostImpl(final FocusEvent e){
                loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW, "tool window focus", "tool window unfocused");
            }

            @Override
            //https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000018010/comments/360000025810
            protected void focusedComponentChanged(Component focusedComponent, @Nullable AWTEvent cause) {
                if (focusedComponent != null && SwingUtilities.isDescendingFrom(focusedComponent, toolWindow.getComponent())) {
                    loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW, "tool window focus", "tool window focused");

                }
            }
        }.install(toolWindow.getComponent());

        toolWindow.getComponent().addPropertyChangeListener(propertyChangeEvent -> {
            System.out.println("addPropertyChangeListener toolWindow: " + propertyChangeEvent.getPropertyName());

        });


        //TODO MAYBE REFACTOR
        //logged open and close of tool window
        toolWindow.getComponent().addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent ancestorEvent) {
                System.out.println("ancestorAdded toolWindow: " + toolWindow.isVisible());
                loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW, "Tool Window Visibility", String.valueOf(toolWindow.isVisible()));
            }

            @Override
            public void ancestorRemoved(AncestorEvent ancestorEvent) {
                System.out.println("ancestorRemoved toolWindow: " + toolWindow.isVisible());
                loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW, "Tool Window Visibility", String.valueOf(toolWindow.isVisible()));
            }

            @Override
            public void ancestorMoved(AncestorEvent ancestorEvent) {
            }
        });
    }

    private void initScreens() {
        handoutContentScreen = new HandoutContentScreen(toolWindow);
        checklistScreen = new ChecklistScreen(toolWindow);
        specificAssessmentCriteriaScreen = new SpecificAssessmentCriteriaScreen(toolWindow);
        helpScreen = new HelpScreen(toolWindow);
        notesScreen = new NotesScreen(toolWindow);
        addScreenContent();
    }

    private void addScreenContent() {
        handoutContent = contentFactory.createContent(handoutContentScreen.getContent(), "Handout", false);
        handoutContent.setPreferredFocusableComponent(handoutContentScreen.getContent());
        checklistContent = contentFactory.createContent(checklistScreen.getContent(), "Aufgaben", false);
        notesContent = contentFactory.createContent(notesScreen.getContent(), "Notizen", false);
        specificCriteriaContent = contentFactory.createContent(specificAssessmentCriteriaScreen.getContent(), "Bewertungskriterien", false);
        commonAssessmentCriteriaContent = contentFactory.createContent(helpScreen.getContent(), "Hilfe", false);

        toolWindow.getContentManager().addContent(handoutContent);
        toolWindow.getContentManager().addContent(checklistContent);
        toolWindow.getContentManager().addContent(specificCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContent);
        toolWindow.getContentManager().addContent(notesContent);
        //TODO: Decide which Tab is open when start ide
        toolWindow.getContentManager().setSelectedContent(handoutContent);
        handoutContent.setDescription("HMMMMm");

        System.out.println("Desrciption: " + handoutContent.getDescription());
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
        helpScreen.updateContent();
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
