package toolWindow.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import gui.NoteAddingFrame;
import javafx.application.Platform;
import objects.Notes;
import org.jetbrains.annotations.NotNull;
import toolWindow.NotesScreen;

import javax.swing.*;

public class AddNotesAction extends AnAction {

    private NotesScreen notesScreen;
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            NoteAddingFrame noteAddingFrame = new NoteAddingFrame(notesScreen);
            noteAddingFrame.showAddNoteFrame();
        });
    }

    //TODO TRY TO GET TOOLWINDOW VIA https://intellij-support.jetbrains.com/hc/en-us/community/posts/115000534584-Access-toolwindow-from-an-action
    // ToolWindowManager.getInstance(project).getToolWindow(TOOLWINDOW_ID)
    public void setNotesScreen(NotesScreen notesScreen){
        this.notesScreen = notesScreen;
    }
}
