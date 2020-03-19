package controller;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorEventMulticaster;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import toolWindow.HandoutContentScreen;

public class LinkToHandoutController{

    private Project project;
    private HandoutContentScreen handoutContentScreen;
    private String functionAnchor;
    private int doubleClick = 2;
    public LinkToHandoutController(Project project, HandoutContentScreen handoutContentScreen){
        this.project = project;
        this.handoutContentScreen = handoutContentScreen;
        createListener();
    }

    //create listener for editors for a double click
    private void createListener() {
       ApplicationManager.getApplication().invokeLater(() -> {
           //https://www.programcreek.com/java-api-examples/?api=com.intellij.openapi.editor.EditorFactory
           EditorEventMulticaster editorEventMulticaster = EditorFactory.getInstance().getEventMulticaster();
           editorEventMulticaster.addEditorMouseListener(new EditorMouseListener() {
               @Override
               public void mouseClicked(@NotNull EditorMouseEvent event) {
                   if (event.getMouseEvent().getClickCount() == doubleClick) {
                       openHandoutOnPosition();
                   }
               }
           });
        });
    }

    //open Handout on the position the link points to
    private void openHandoutOnPosition() {
        //https://intellij-support.jetbrains.com/hc/en-us/community/posts/206768435/comments/206128935
        FileEditor currentFileEditor = FileEditorManager.getInstance(project).getSelectedEditor();
        Editor currentEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        String selectedText = currentEditor.getSelectionModel().getSelectedText();
        String className = currentFileEditor.getFile().getPresentableName();
        if(selectedText != null) {
            functionAnchor = className + "/" + selectedText;
        } else {
            functionAnchor = null;
        }
        if (functionAnchor != null) {
            handoutContentScreen.goToLocation(functionAnchor);
        }
    }
}
