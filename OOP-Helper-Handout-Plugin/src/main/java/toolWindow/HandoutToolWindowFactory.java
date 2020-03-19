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
import controller.UpdateHandoutDataController;
import de.ur.mi.pluginhelper.logger.LogDataType;
import eventHandling.OnLocalDataUpdatedListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.FocusEvent;

import static environment.LoggingMessageConstants.*;

public class HandoutToolWindowFactory implements ToolWindowFactory, OnLocalDataUpdatedListener {

    private OnLocalDataUpdatedListener listener;

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
    private UpdateHandoutDataController updateHandoutDataController;

    private static String UPDATE_ACTION_NAME = "Myplugin.Textboxes.Update";
    private static String MINIMIZE_ACTION_NAME = "Handout.Minimize";
    private static String MAXIMIZE_ACTION_NAME = "Handout.Maximize";

    private static String HANDOUT_CONTENT_NAME = "Handout";
    private static String TASK_CONTENT_NAME = "Aufgaben";
    private static String NOTES_CONTENT_NAME = "Notizen";
    private static String ASSESSMENT_CRITERIA_CONTENT_NAME = "Bewertungskriterien";
    private static String HELP_CONTENT_NAME = "Hilfe";

    // Create the tool window content.
    // is called when toolWindow opens
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        contentFactory = ContentFactory.SERVICE.getInstance();
        initScreens();
        createToolbar();
        createToolWindowListener();
        loggingController = LoggingController.getInstance();
        updateHandoutDataController = UpdateHandoutDataController.getInstance();
        updateHandoutDataController.addListener(this);
    }


    private void createToolWindowListener() {
        logTabChanges();
        logToolWindowVisibility();
        logToolWindowFocus();
    }

    private void logToolWindowFocus() {
        new FocusWatcher() {
            @Override
            protected void focusLostImpl(final FocusEvent e) {
                loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW, TOOL_WINDOW_FOCUS,  UNFOCUSED);
            }
            @Override
            //https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000018010/comments/360000025810
            protected void focusedComponentChanged(Component focusedComponent, @Nullable AWTEvent cause) {
                if (focusedComponent != null && SwingUtilities.isDescendingFrom(focusedComponent, toolWindow.getComponent())) {
                    loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW, TOOL_WINDOW_FOCUS, FOCUSED);

                }
            }
        }.install(toolWindow.getComponent());
    }

    private void logToolWindowVisibility() {
        //logs visibility of tool window
        toolWindow.getComponent().addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent ancestorEvent) {
                loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW,TOOL_WINDOW_VISIBILITY, String.valueOf(toolWindow.isVisible()));
            }

            @Override
            public void ancestorRemoved(AncestorEvent ancestorEvent) {
                loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW, TOOL_WINDOW_VISIBILITY, String.valueOf(toolWindow.isVisible()));
            }

            @Override
            public void ancestorMoved(AncestorEvent ancestorEvent) {
            }
        });
    }

    // logs tab changes in toolWindow
    private void logTabChanges() {
        toolWindow.getContentManager().addContentManagerListener(new ContentManagerListener() {
            @Override
            public void contentAdded(@NotNull ContentManagerEvent event) { }

            @Override
            public void contentRemoved(@NotNull ContentManagerEvent event) { }

            @Override
            public void contentRemoveQuery(@NotNull ContentManagerEvent event) { }

            @Override
            public void selectionChanged(@NotNull ContentManagerEvent event) {
                if (event.getContent().isSelected()) {
                    loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW, TAB_FOCUS_LOST, event.getContent().getDisplayName());
                } else {
                    loggingController.saveDataInLogger(LogDataType.TOOL_WINDOW, TAB_FOCUS_GAINED, event.getContent().getDisplayName());
                }
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
        handoutContent = contentFactory.createContent(handoutContentScreen.getContent(), HANDOUT_CONTENT_NAME, false);
        handoutContent.setPreferredFocusableComponent(handoutContentScreen.getContent());
        checklistContent = contentFactory.createContent(checklistScreen.getContent(), TASK_CONTENT_NAME, false);
        notesContent = contentFactory.createContent(notesScreen.getContent(), NOTES_CONTENT_NAME, false);
        specificCriteriaContent = contentFactory.createContent(specificAssessmentCriteriaScreen.getContent(), ASSESSMENT_CRITERIA_CONTENT_NAME, false);
        commonAssessmentCriteriaContent = contentFactory.createContent(helpScreen.getContent(), HELP_CONTENT_NAME, false);

        toolWindow.getContentManager().addContent(handoutContent);
        toolWindow.getContentManager().addContent(checklistContent);
        toolWindow.getContentManager().addContent(specificCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContent);
        toolWindow.getContentManager().addContent(notesContent);
        toolWindow.getContentManager().setSelectedContent(handoutContent);
        callListener();
    }

    private void createToolbar() {
        AnAction updateAction = ActionManager.getInstance().getAction(UPDATE_ACTION_NAME);
        AnAction minimizeAction = (ActionManager.getInstance().getAction(MINIMIZE_ACTION_NAME));
        AnAction maximizeAction = (ActionManager.getInstance().getAction(MAXIMIZE_ACTION_NAME));
        ((ToolWindowEx) toolWindow).setTitleActions(new AnAction[]{updateAction, minimizeAction, maximizeAction});
    }

    //updates the webViews if new content data was downloaded
    public void updateWebView() {
        handoutContentScreen.updateContent();
        specificAssessmentCriteriaScreen.updateContent();
        helpScreen.updateContent();
        checklistScreen.updateContent();
    }

    private void callListener() {
        if (listener != null) {
            listener.OnLocalDataUpdatedEvent();
        }
    }

    @Override
    public void OnLocalDataUpdatedEvent() {
        updateWebView();
    }
}
