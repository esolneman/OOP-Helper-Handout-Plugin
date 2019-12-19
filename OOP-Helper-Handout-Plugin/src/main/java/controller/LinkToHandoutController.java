package controller;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import de.ur.mi.pluginhelper.logger.LogDataType;
import org.jetbrains.annotations.NotNull;
import toolWindow.HandoutContentScreen;

public class LinkToHandoutController{

    private Project project;
    private HandoutContentScreen handoutContentScreen;
    private String functionAnchor;

    public LinkToHandoutController(Project project, HandoutContentScreen handoutContentScreen){
        this.project = project;
        this.handoutContentScreen = handoutContentScreen;
        createListener();
    }

    private void createListener() {
        System.out.println("Create Listener for Link from Code -> Handout");
        //working but first to register opened editorss
        //TODO CHECK IF NOT TOLLWINDOW
       ApplicationManager.getApplication().invokeLater(() -> {
           //https://www.programcreek.com/java-api-examples/?api=com.intellij.openapi.editor.EditorFactory
           EditorEventMulticaster editorEventMulticaster = EditorFactory.getInstance().getEventMulticaster();
           /*editorEventMulticaster.addSelectionListener(new SelectionListener() {
                @Override
                public void selectionChanged(@NotNull SelectionEvent e) {
                    String selectedText = e.getEditor().getSelectionModel().getSelectedText();
                    String className = FileEditorManager.getInstance(project).getSelectedEditor().getFile().getPresentableName();
                    System.out.println("selectedText: " + selectedText);
                    System.out.println("className: " + className);
                    if(selectedText != null) {
                        functionAnchor = className + "/" + selectedText;
                        System.out.println("functionAnchorSelect: " + functionAnchor);
                    } else {
                        functionAnchor = null;
                        System.out.println("functionAnchorSelect: " + functionAnchor);
                    }
                }
            });*/
            //TODO: check Disposable
           //TODO Remove Listener when close Project
           editorEventMulticaster.addEditorMouseListener(new EditorMouseListener() {
               @Override
               public void mouseClicked(@NotNull EditorMouseEvent event) {
                   if (event.getMouseEvent().getClickCount() == 2) {
                       openHandoutOnPosition();
                   }
               }
           });
        });
    }

    private void openHandoutOnPosition() {
        System.out.println(" openHandoutOnPosition functionAnchor: " + functionAnchor);
        //https://intellij-support.jetbrains.com/hc/en-us/community/posts/206768435/comments/206128935
        //https://www.programcreek.com/java-api-examples/?api=com.intellij.openapi.editor.EditorFactory

        FileEditor currentFileEditor = FileEditorManager.getInstance(project).getSelectedEditor();
        Editor currentEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        String selectedText = currentEditor.getSelectionModel().getSelectedText();
        String className = currentFileEditor.getFile().getPresentableName();
        if(selectedText != null) {
            functionAnchor = className + "/" + selectedText;
        } else {
            functionAnchor = null;
        }
        System.out.println("functionAnchorSelect: " + functionAnchor);
        if (functionAnchor != null) {
            LoggingController.getInstance().saveDataInLogger(LogDataType.HANDOUT, "Link from Code to Handout", functionAnchor);
            handoutContentScreen.goToLocation(functionAnchor);
        }
    }
}
