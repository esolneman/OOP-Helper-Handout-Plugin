package controller;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import listener.OnEditorClickedListener;
import org.jetbrains.annotations.NotNull;

public class LinkToHandoutController implements OnEditorClickedListener {

    private Project project;

    public LinkToHandoutController(Project project){
        this.project = project;
        createListener();
    }

    private void createListener() {
        System.out.println("Create Listener for Link from Code -> Handout");

        //working but first to register opened editorss
       ApplicationManager.getApplication().invokeLater(() -> {
           /* final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
            System.out.println("FileEditorManager: " + fileEditorManager.getClass());
            EditorFactoryListener editorFactoryListener = new EditorFactoryListener() {
                @Override
                public void editorCreated(@NotNull EditorFactoryEvent event) {
                    System.out.println("editorCreated: " + event.getEditor());
                    Editor newEditor = event.getEditor();
                    EditorMouseListener editorMouseListener = new EditorMouseListener() {
                        @Override
                        public void mousePressed(@NotNull EditorMouseEvent mouseevent) {
                            System.out.println("mousePressed: " + mouseevent.getArea().toString());

                        }
                    };
                    newEditor.addEditorMouseListener(editorMouseListener);
                }
            };
            Disposable disposable = () -> {};
            EditorFactory.getInstance().addEditorFactoryListener(editorFactoryListener, disposable);*/


            EditorEventMulticaster editorEventMulticaster = EditorFactory.getInstance().getEventMulticaster();
            editorEventMulticaster.addEditorMouseListener(new EditorMouseListener() {
                @Override
                public void mousePressed(@NotNull EditorMouseEvent event) {
                    System.out.println("addEditorMouseListener: " + event.toString());
                }
            });
            editorEventMulticaster.addSelectionListener(new SelectionListener() {
                @Override
                public void selectionChanged(@NotNull SelectionEvent e) {
                    System.out.println("SelectionEvent: " + e.toString());
                    String selectedText = e.getEditor().getSelectionModel().getSelectedText();
                    String className = FileEditorManager.getInstance(project).getSelectedEditor().getName();
                    System.out.println("selectedText: " + selectedText);
                    System.out.println("className: " + FileEditorManager.getInstance(project).getSelectedEditor().getFile().getPresentableName());
                    String functionAnchor = className + "." + selectedText;

                    if(selectedText != null) {
                        openHandoutOnPosition(functionAnchor);
                    }
                }
            });

            //TODO: check Disposable

        });
    }

    private void openHandoutOnPosition(String functionAnchor) {
        
    }

    @Override
    public void onFunctionClickedEvent(String functionName, String className, String path) {
        System.out.println(functionName);

    }
}
