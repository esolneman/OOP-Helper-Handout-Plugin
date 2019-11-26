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
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;


public class HandoutToolWindowFactory implements ToolWindowFactory, ToolWindowServiceInterface {

    private List<OnToolWindowCreatedListener> listeners = new ArrayList<>();

    private ToolWindow toolWindow;
    private ContentFactory contentFactory;

    private Content handoutContent;
    private Content checklistContent;
    //private Content shortcutContent;
    private Content specificCriteriaContent;
    private Content commonAssessmentCriteriaContent;
    private Content notesContent;


    private HandoutContentScreen handoutContentScreen;
    private ChecklistScreen checklistScreen;
    //private ShortcutScreen shortcutScreen;
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
            public void contentAdded(@NotNull ContentManagerEvent event) {}

            @Override
            public void contentRemoved(@NotNull ContentManagerEvent event) {}

            @Override
            public void contentRemoveQuery(@NotNull ContentManagerEvent event) {}

            @Override
            public void selectionChanged(@NotNull ContentManagerEvent event) {
                System.out.println("in: selectionChanged");
                if(event.getContent().isSelected()){
                    System.out.println("Current Component Gained: \"" + event.getContent().getDisplayName() + "\"");
                    loggingController.saveDataInLogger(LogDataType.CUSTOM, "focus lost", event.getContent().getDisplayName());
                }else{
                    System.out.println("Current Component Lost: \"" + event.getContent().getDisplayName() + "\"");
                    loggingController.saveDataInLogger(LogDataType.CUSTOM, "focus gained", event.getContent().getDisplayName());
                }
            }
        });
    }

    private void initScreens() {
        handoutContentScreen = new HandoutContentScreen(toolWindow);
        //shortcutScreen = new ShortcutScreen(toolWindow);
        checklistScreen = new ChecklistScreen(toolWindow);
        specificAssessmentCriteriaScreen = new SpecificAssessmentCriteriaScreen(toolWindow);
        commonAssessmentCriteriaScreen = new HelpScreen(toolWindow);
        notesScreen = new NotesScreen(toolWindow);
        addScreenContent();
    }

    private void addScreenContent() {
        handoutContent = contentFactory.createContent(handoutContentScreen.getContent(), "Handout", false);
        handoutContent.setPreferredFocusableComponent(handoutContentScreen.getContent());
        checklistContent = contentFactory.createContent(checklistScreen.getContent(), "Aufgaben", false);
        //shortcutContent = contentFactory.createContent(shortcutScreen.getContent(), "Shortcut", false);
        specificCriteriaContent = contentFactory.createContent(specificAssessmentCriteriaScreen.getContent(), "Bewertungskriterien", false);
        commonAssessmentCriteriaContent = contentFactory.createContent(commonAssessmentCriteriaScreen.getContent(), "Hilfe", false);
        notesContent = contentFactory.createContent(notesScreen.getContent(), "Notizen", false);

        toolWindow.getContentManager().addContent(handoutContent);
        toolWindow.getContentManager().addContent(checklistContent);
        //toolWindow.getContentManager().addContent(shortcutContent);
        toolWindow.getContentManager().addContent(specificCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContent);
        toolWindow.getContentManager().addContent(notesContent);
        //TODO: Decide which Tab is open when start ide
        toolWindow.getContentManager().setSelectedContent(handoutContent);

        System.out.println("toolWindow.getContentManager() Content Length: " + toolWindow.getContentManager().getContents().length);


        /*//https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000018010/comments/360000025810
        for (Content content : toolWindow.getContentManager().getContents()) {
            System.out.println("toolWindow content: " + content.getDisplayName());
            System.out.println("toolWindow content Component: " + content.getComponent().getName());

            content.getComponent().addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent focusEvent) {
                    System.out.println("focusGained Content: " + content.getDisplayName());
                }

                @Override
                public void focusLost(FocusEvent focusEvent) {
                    System.out.println("focusLost Content: " + content.getDisplayName());
                }
            });
        }*/


        /*toolWindow.getComponent().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                System.out.println("focusGained: " + focusEvent.toString());
                System.out.println("focusGained: " + focusEvent.getSource().toString());

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                System.out.println("focusLost: " + focusEvent.toString());
                System.out.println("focusLost: " + focusEvent.getSource().toString());

            }
        });
*/
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
