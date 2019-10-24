package controller;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import listener.OnEditorClickedListener;

public class LinkToHandoutController implements OnEditorClickedListener {

    private Project project;

    public LinkToHandoutController(Project project){
        this.project = project;
        createListener();
    }

    private void createListener() {
        System.out.println("Create Listener for Link from Code -> Handout");
        ApplicationManager.getApplication().invokeLater(() -> {
            final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
            System.out.println("FileEditorManager: " + fileEditorManager.getClass());
            final FileEditor[] editor = fileEditorManager.getAllEditors();
            System.out.println("editor: " + editor.length);
            for (FileEditor fileEditor : editor) {
                System.out.println(fileEditor.getName());
            }
            //final Editor textEditor = ((TextEditor) editor).getEditor();
        });
    }

    @Override
    public void onFunctionClickedEvent(String functionName, String className, String path) {
        System.out.println(functionName);

    }
}
